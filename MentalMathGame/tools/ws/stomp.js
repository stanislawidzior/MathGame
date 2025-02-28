// Generated by CoffeeScript 1.12.6

/*
   Stomp Over WebSocket http://www.jmesnil.net/stomp-websocket/doc/ | Apache License V2.0

   Copyright (C) 2010-2013 [Jeff Mesnil](http://jmesnil.net/)
   Copyright (C) 2012 [FuseSource, Inc.](http://fusesource.com)
   Copyright (C) 2017 [Deepak Kumar](https://www.kreatio.com)
 */

   (function() {
    var Byte, Client, Frame, Stomp,
      hasProp = {}.hasOwnProperty,
      slice = [].slice;
  
    Byte = {
      LF: '\x0A',
      NULL: '\x00'
    };
  
    Frame = (function() {
      var unmarshallSingle;
  
      function Frame(command1, headers1, body1, escapeHeaderValues1) {
        this.command = command1;
        this.headers = headers1 != null ? headers1 : {};
        this.body = body1 != null ? body1 : '';
        this.escapeHeaderValues = escapeHeaderValues1 != null ? escapeHeaderValues1 : false;
      }
  
      Frame.prototype.toString = function() {
        var lines, name, ref, skipContentLength, value;
        lines = [this.command];
        skipContentLength = (this.headers['content-length'] === false) ? true : false;
        if (skipContentLength) {
          delete this.headers['content-length'];
        }
        ref = this.headers;
        for (name in ref) {
          if (!hasProp.call(ref, name)) continue;
          value = ref[name];
          if (this.escapeHeaderValues && this.command !== 'CONNECT' && this.command !== 'CONNECTED') {
            lines.push(name + ":" + (Frame.frEscape(value)));
          } else {
            lines.push(name + ":" + value);
          }
        }
        if (this.body && !skipContentLength) {
          lines.push("content-length:" + (Frame.sizeOfUTF8(this.body)));
        }
        lines.push(Byte.LF + this.body);
        return lines.join(Byte.LF);
      };
  
      Frame.sizeOfUTF8 = function(s) {
        if (s) {
          return encodeURI(s).match(/%..|./g).length;
        } else {
          return 0;
        }
      };
  
      unmarshallSingle = function(data, escapeHeaderValues) {
        var body, chr, command, divider, headerLines, headers, i, idx, j, k, len, len1, line, ref, ref1, ref2, start, trim;
        if (escapeHeaderValues == null) {
          escapeHeaderValues = false;
        }
        divider = data.search(RegExp("" + Byte.LF + Byte.LF));
        headerLines = data.substring(0, divider).split(Byte.LF);
        command = headerLines.shift();
        headers = {};
        trim = function(str) {
          return str.replace(/^\s+|\s+$/g, '');
        };
        ref = headerLines.reverse();
        for (j = 0, len1 = ref.length; j < len1; j++) {
          line = ref[j];
          idx = line.indexOf(':');
          if (escapeHeaderValues && command !== 'CONNECT' && command !== 'CONNECTED') {
            headers[trim(line.substring(0, idx))] = Frame.frUnEscape(trim(line.substring(idx + 1)));
          } else {
            headers[trim(line.substring(0, idx))] = trim(line.substring(idx + 1));
          }
        }
        body = '';
        start = divider + 2;
        if (headers['content-length']) {
          len = parseInt(headers['content-length']);
          body = ('' + data).substring(start, start + len);
        } else {
          chr = null;
          for (i = k = ref1 = start, ref2 = data.length; ref1 <= ref2 ? k < ref2 : k > ref2; i = ref1 <= ref2 ? ++k : --k) {
            chr = data.charAt(i);
            if (chr === Byte.NULL) {
              break;
            }
            body += chr;
          }
        }
        return new Frame(command, headers, body, escapeHeaderValues);
      };
  
      Frame.unmarshall = function(datas, escapeHeaderValues) {
        var frame, frames, last_frame, r;
        if (escapeHeaderValues == null) {
          escapeHeaderValues = false;
        }
        frames = datas.split(RegExp("" + Byte.NULL + Byte.LF + "*"));
        r = {
          frames: [],
          partial: ''
        };
        r.frames = (function() {
          var j, len1, ref, results;
          ref = frames.slice(0, -1);
          results = [];
          for (j = 0, len1 = ref.length; j < len1; j++) {
            frame = ref[j];
            results.push(unmarshallSingle(frame, escapeHeaderValues));
          }
          return results;
        })();
        last_frame = frames.slice(-1)[0];
        if (last_frame === Byte.LF || (last_frame.search(RegExp("" + Byte.NULL + Byte.LF + "*$"))) !== -1) {
          r.frames.push(unmarshallSingle(last_frame, escapeHeaderValues));
        } else {
          r.partial = last_frame;
        }
        return r;
      };
  
      Frame.marshall = function(command, headers, body, escapeHeaderValues) {
        var frame;
        frame = new Frame(command, headers, body, escapeHeaderValues);
        return frame.toString() + Byte.NULL;
      };
  
      Frame.frEscape = function(str) {
        return ("" + str).replace(/\\/g, "\\\\").replace(/\r/g, "\\r").replace(/\n/g, "\\n").replace(/:/g, "\\c");
      };
  
      Frame.frUnEscape = function(str) {
        return ("" + str).replace(/\\r/g, "\r").replace(/\\n/g, "\n").replace(/\\c/g, ":").replace(/\\\\/g, "\\");
      };
  
      return Frame;
  
    })();
  
    Client = (function() {
      var now;
  
      function Client(ws_fn) {
        this.ws_fn = function() {
          var ws;
          ws = ws_fn();
          ws.binaryType = "arraybuffer";
          return ws;
        };
        this.reconnect_delay = 0;
        this.counter = 0;
        this.connected = false;
        this.heartbeat = {
          outgoing: 10000,
          incoming: 10000
        };
        this.maxWebSocketFrameSize = 16 * 1024;
        this.subscriptions = {};
        this.partialData = '';
      }
  
      Client.prototype.debug = function(message) {
        var ref;
        return typeof window !== "undefined" && window !== null ? (ref = window.console) != null ? ref.log(message) : void 0 : void 0;
      };
  
      now = function() {
        if (Date.now) {
          return Date.now();
        } else {
          return new Date().valueOf;
        }
      };
  
      Client.prototype._transmit = function(command, headers, body) {
        var out;
        out = Frame.marshall(command, headers, body, this.escapeHeaderValues);
        if (typeof this.debug === "function") {
          this.debug(">>> " + out);
        }
        while (true) {
          if (out.length > this.maxWebSocketFrameSize) {
            this.ws.send(out.substring(0, this.maxWebSocketFrameSize));
            out = out.substring(this.maxWebSocketFrameSize);
            if (typeof this.debug === "function") {
              this.debug("remaining = " + out.length);
            }
          } else {
            return this.ws.send(out);
          }
        }
      };
  
      Client.prototype._setupHeartbeat = function(headers) {
        var ref, ref1, serverIncoming, serverOutgoing, ttl, v;
        if ((ref = headers.version) !== Stomp.VERSIONS.V1_1 && ref !== Stomp.VERSIONS.V1_2) {
          return;
        }
        ref1 = (function() {
          var j, len1, ref1, results;
          ref1 = headers['heart-beat'].split(",");
          results = [];
          for (j = 0, len1 = ref1.length; j < len1; j++) {
            v = ref1[j];
            results.push(parseInt(v));
          }
          return results;
        })(), serverOutgoing = ref1[0], serverIncoming = ref1[1];
        if (!(this.heartbeat.outgoing === 0 || serverIncoming === 0)) {
          ttl = Math.max(this.heartbeat.outgoing, serverIncoming);
          if (typeof this.debug === "function") {
            this.debug("send PING every " + ttl + "ms");
          }
          this.pinger = Stomp.setInterval(ttl, (function(_this) {
            return function() {
              _this.ws.send(Byte.LF);
              return typeof _this.debug === "function" ? _this.debug(">>> PING") : void 0;
            };
          })(this));
        }
        if (!(this.heartbeat.incoming === 0 || serverOutgoing === 0)) {
          ttl = Math.max(this.heartbeat.incoming, serverOutgoing);
          if (typeof this.debug === "function") {
            this.debug("check PONG every " + ttl + "ms");
          }
          return this.ponger = Stomp.setInterval(ttl, (function(_this) {
            return function() {
              var delta;
              delta = now() - _this.serverActivity;
              if (delta > ttl * 2) {
                if (typeof _this.debug === "function") {
                  _this.debug("did not receive server activity for the last " + delta + "ms");
                }
                return _this.ws.close();
              }
            };
          })(this));
        }
      };
  
      Client.prototype._parseConnect = function() {
        var args, closeEventCallback, connectCallback, errorCallback, headers;
        args = 1 <= arguments.length ? slice.call(arguments, 0) : [];
        headers = {};
        if (args.length < 2) {
          throw "Connect requires at least 2 arguments";
        }
        if (typeof args[1] === 'function') {
          headers = args[0], connectCallback = args[1], errorCallback = args[2], closeEventCallback = args[3];
        } else {
          switch (args.length) {
            case 6:
              headers.login = args[0], headers.passcode = args[1], connectCallback = args[2], errorCallback = args[3], closeEventCallback = args[4], headers.host = args[5];
              break;
            default:
              headers.login = args[0], headers.passcode = args[1], connectCallback = args[2], errorCallback = args[3], closeEventCallback = args[4];
          }
        }
        return [headers, connectCallback, errorCallback, closeEventCallback];
      };
  
      Client.prototype.connect = function() {
        var args, out;
        args = 1 <= arguments.length ? slice.call(arguments, 0) : [];
        this.escapeHeaderValues = false;
        out = this._parseConnect.apply(this, args);
        this.headers = out[0], this.connectCallback = out[1], this.errorCallback = out[2], this.closeEventCallback = out[3];
        this._active = true;
        return this._connect();
      };
  
      Client.prototype._connect = function() {
        var UTF8ArrayToStr, closeEventCallback, errorCallback, headers;
        headers = this.headers;
        errorCallback = this.errorCallback;
        closeEventCallback = this.closeEventCallback;
        if (!this._active) {
          this.debug('Client has been marked inactive, will not attempt to connect');
          return;
        }
        if (typeof this.debug === "function") {
          this.debug("Opening Web Socket...");
        }
        this.ws = this.ws_fn();
        UTF8ArrayToStr = (function(_this) {
          return function(array) {
            var c, char2, char3, i, len, out;
            out = "";
            len = array.length;
            i = 0;
            while (i < len) {
              c = array[i++];
              switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                  out += String.fromCharCode(c);
                  break;
                case 12:
                case 13:
                  char2 = array[i++];
                  out += String.fromCharCode(((c & 0x1F) << 6) | (char2 & 0x3F));
                  break;
                case 14:
                  char2 = array[i++];
                  char3 = array[i++];
                  out += String.fromCharCode(((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
              }
            }
            return out;
          };
        })(this);
        this.ws.onmessage = (function(_this) {
          return function(evt) {
            var arr, client, data, frame, j, len1, messageID, onreceive, ref, subscription, unmarshalledData;
            data = typeof ArrayBuffer !== 'undefined' && evt.data instanceof ArrayBuffer ? (arr = new Uint8Array(evt.data), typeof _this.debug === "function" ? _this.debug("--- got data length: " + arr.length) : void 0, UTF8ArrayToStr(arr)) : evt.data;
            _this.serverActivity = now();
            if (data === Byte.LF) {
              if (typeof _this.debug === "function") {
                _this.debug("<<< PONG");
              }
              return;
            }
            if (typeof _this.debug === "function") {
              _this.debug("<<< " + data);
            }
            unmarshalledData = Frame.unmarshall(_this.partialData + data, _this.escapeHeaderValues);
            _this.partialData = unmarshalledData.partial;
            ref = unmarshalledData.frames;
            for (j = 0, len1 = ref.length; j < len1; j++) {
              frame = ref[j];
              switch (frame.command) {
                case "CONNECTED":
                  if (typeof _this.debug === "function") {
                    _this.debug("connected to server " + frame.headers.server);
                  }
                  _this.connected = true;
                  _this.version = frame.headers.version;
                  if (_this.version === Stomp.VERSIONS.V1_2) {
                    _this.escapeHeaderValues = true;
                  }
                  if (!_this._active) {
                    _this.disconnect();
                    return;
                  }
                  _this._setupHeartbeat(frame.headers);
                  if (typeof _this.connectCallback === "function") {
                    _this.connectCallback(frame);
                  }
                  break;
                case "MESSAGE":
                  subscription = frame.headers.subscription;
                  onreceive = _this.subscriptions[subscription] || _this.onreceive;
                  if (onreceive) {
                    client = _this;
                    if (_this.version === Stomp.VERSIONS.V1_2) {
                      messageID = frame.headers["ack"];
                    } else {
                      messageID = frame.headers["message-id"];
                    }
                    frame.ack = function(headers) {
                      if (headers == null) {
                        headers = {};
                      }
                      return client.ack(messageID, subscription, headers);
                    };
                    frame.nack = function(headers) {
                      if (headers == null) {
                        headers = {};
                      }
                      return client.nack(messageID, subscription, headers);
                    };
                    onreceive(frame);
                  } else {
                    if (typeof _this.debug === "function") {
                      _this.debug("Unhandled received MESSAGE: " + frame);
                    }
                  }
                  break;
                case "RECEIPT":
                  if (frame.headers["receipt-id"] === _this.closeReceipt) {
                    _this.ws.onclose = null;
                    _this.ws.close();
                    _this._cleanUp();
                    if (typeof _this._disconnectCallback === "function") {
                      _this._disconnectCallback();
                    }
                  } else {
                    if (typeof _this.onreceipt === "function") {
                      _this.onreceipt(frame);
                    }
                  }
                  break;
                case "ERROR":
                  if (typeof errorCallback === "function") {
                    errorCallback(frame);
                  }
                  break;
                default:
                  if (typeof _this.debug === "function") {
                    _this.debug("Unhandled frame: " + frame);
                  }
              }
            }
          };
        })(this);
        this.ws.onclose = (function(_this) {
          return function(closeEvent) {
            var msg;
            msg = "Whoops! Lost connection to " + _this.ws.url;
            if (typeof _this.debug === "function") {
              _this.debug(msg);
            }
            if (typeof closeEventCallback === "function") {
              closeEventCallback(closeEvent);
            }
            _this._cleanUp();
            if (typeof errorCallback === "function") {
              errorCallback(msg);
            }
            return _this._schedule_reconnect();
          };
        })(this);
        return this.ws.onopen = (function(_this) {
          return function() {
            if (typeof _this.debug === "function") {
              _this.debug('Web Socket Opened...');
            }
            headers["accept-version"] = Stomp.VERSIONS.supportedVersions();
            headers["heart-beat"] = [_this.heartbeat.outgoing, _this.heartbeat.incoming].join(',');
            return _this._transmit("CONNECT", headers);
          };
        })(this);
      };
  
      Client.prototype._schedule_reconnect = function() {
        if (this.reconnect_delay > 0) {
          if (typeof this.debug === "function") {
            this.debug("STOMP: scheduling reconnection in " + this.reconnect_delay + "ms");
          }
          return this._reconnector = setTimeout((function(_this) {
            return function() {
              if (_this.connected) {
                return typeof _this.debug === "function" ? _this.debug('STOMP: already connected') : void 0;
              } else {
                if (typeof _this.debug === "function") {
                  _this.debug('STOMP: attempting to reconnect');
                }
                return _this._connect();
              }
            };
          })(this), this.reconnect_delay);
        }
      };
  
      Client.prototype.disconnect = function(disconnectCallback, headers) {
        var error;
        if (headers == null) {
          headers = {};
        }
        this._disconnectCallback = disconnectCallback;
        this._active = false;
        if (this.connected) {
          if (!headers.receipt) {
            headers.receipt = "close-" + this.counter++;
          }
          this.closeReceipt = headers.receipt;
          try {
            return this._transmit("DISCONNECT", headers);
          } catch (error1) {
            error = error1;
            return typeof this.debug === "function" ? this.debug('Ignoring error during disconnect', error) : void 0;
          }
        }
      };
  
      Client.prototype._cleanUp = function() {
        if (this._reconnector) {
          clearTimeout(this._reconnector);
        }
        this.connected = false;
        this.subscriptions = {};
        this.partial = '';
        if (this.pinger) {
          Stomp.clearInterval(this.pinger);
        }
        if (this.ponger) {
          return Stomp.clearInterval(this.ponger);
        }
      };
  
      Client.prototype.send = function(destination, headers, body) {
        if (headers == null) {
          headers = {};
        }
        if (body == null) {
          body = '';
        }
        headers.destination = destination;
        return this._transmit("SEND", headers, body);
      };
  
      Client.prototype.subscribe = function(destination, callback, headers) {
        var client;
        if (headers == null) {
          headers = {};
        }
        if (!headers.id) {
          headers.id = "sub-" + this.counter++;
        }
        headers.destination = destination;
        this.subscriptions[headers.id] = callback;
        this._transmit("SUBSCRIBE", headers);
        client = this;
        return {
          id: headers.id,
          unsubscribe: function(hdrs) {
            return client.unsubscribe(headers.id, hdrs);
          }
        };
      };
  
      Client.prototype.unsubscribe = function(id, headers) {
        if (headers == null) {
          headers = {};
        }
        delete this.subscriptions[id];
        headers.id = id;
        return this._transmit("UNSUBSCRIBE", headers);
      };
  
      Client.prototype.begin = function(transaction_id) {
        var client, txid;
        txid = transaction_id || "tx-" + this.counter++;
        this._transmit("BEGIN", {
          transaction: txid
        });
        client = this;
        return {
          id: txid,
          commit: function() {
            return client.commit(txid);
          },
          abort: function() {
            return client.abort(txid);
          }
        };
      };
  
      Client.prototype.commit = function(transaction_id) {
        return this._transmit("COMMIT", {
          transaction: transaction_id
        });
      };
  
      Client.prototype.abort = function(transaction_id) {
        return this._transmit("ABORT", {
          transaction: transaction_id
        });
      };
  
      Client.prototype.ack = function(messageID, subscription, headers) {
        if (headers == null) {
          headers = {};
        }
        if (this.version === Stomp.VERSIONS.V1_2) {
          headers["id"] = messageID;
        } else {
          headers["message-id"] = messageID;
        }
        headers.subscription = subscription;
        return this._transmit("ACK", headers);
      };
  
      Client.prototype.nack = function(messageID, subscription, headers) {
        if (headers == null) {
          headers = {};
        }
        if (this.version === Stomp.VERSIONS.V1_2) {
          headers["id"] = messageID;
        } else {
          headers["message-id"] = messageID;
        }
        headers.subscription = subscription;
        return this._transmit("NACK", headers);
      };
  
      return Client;
  
    })();
  
    Stomp = {
      VERSIONS: {
        V1_0: '1.0',
        V1_1: '1.1',
        V1_2: '1.2',
        supportedVersions: function() {
          return '1.2,1.1,1.0';
        }
      },
      client: function(url, protocols) {
        var ws_fn;
        if (protocols == null) {
          protocols = ['v10.stomp', 'v11.stomp', 'v12.stomp'];
        }
        ws_fn = function() {
          var klass;
          klass = Stomp.WebSocketClass || WebSocket;
          return new klass(url, protocols);
        };
        return new Client(ws_fn);
      },
      over: function(ws) {
        var ws_fn;
        ws_fn = typeof ws === "function" ? ws : function() {
          return ws;
        };
        return new Client(ws_fn);
      },
      Frame: Frame
    };
  
    Stomp.setInterval = function(interval, f) {
      return setInterval(f, interval);
    };
  
    Stomp.clearInterval = function(id) {
      return clearInterval(id);
    };
  
    if (typeof exports !== "undefined" && exports !== null) {
      exports.Stomp = Stomp;
    }
  
    if (typeof window !== "undefined" && window !== null) {
      window.Stomp = Stomp;
    } else if (!exports) {
      self.Stomp = Stomp;
    }
  
  }).call(this);