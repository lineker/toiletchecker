/**
 * Created by pteyssedre on 15-02-03.
 */
var spark = require("spark");
var gcm = require("node-gcm");
var _device;
var _lastCheck = -1;
var _vacantTime = -1;
var _nonVacantTime = -1;
var _timeStatus = 0;
var _stateBroadcast = false;
var _notify = -1;

var sender = new gcm.Sender('insert Google Server API Key here');
var registrationIds = [];

var callback = function (err, body) {
    if (err) {
        console.error(err);
        return;
    }
    spark.listDevices(function (err, devices) {
        _device = devices[0];
        _device.getVariable('status', function (err, data) {
            if (err) {
                console.log('An error occurred while getting attrs:', err);
            } else {
                console.log('Toilet are', (data.result == 0 ? "vacant" : "occupied"));
                _lastCheck = data.result;
                checkLoop((data.result == 0 ? _vacantTime : _nonVacantTime),5000);
            }
        });
        // subscribe is not supported yet by the spark module
        // device.subscribe('status', function (err, data) {
        //     if (err) {
        //         console.log('An error occurred while getting attrs:', err);
        //     } else {
        //         console.log('Toilet are', (data.result == 0 ? "vacant" : "occupied"));
        //         var message = new gcm.Message();
        //         message.addData('occupation',data.result);
        //         sender.send(message, registrationIds, function (err, result) {
        //             if(err) console.error(err);
        //             else    console.log(result);
        //         });
        //     }
        // });

    });
};
spark.login({accessToken: 'a6c96c783d1ca41a1f36cafd0fcb101eefa04568'}, callback);

function checkLoop(pid,time){
    pid = setInterval(function(){
            _device.getVariable('status', function (err, data) {
                if (err) {
                    console.log('An error occurred while getting attrs:', err);
                } else {
                    console.log('Toilet are', (data.result == 0 ? "vacant" : "occupied"));
                    if(_lastCheck != data.result){
                        clearTimeout(_notify);
                        _timeStatus = 0;
                        _lastCheck = data.result;
                        _stateBroadcast = false;
                    }else{
                        _timeStatus += time;
                        if(_timeStatus > time){
                            _notify = setTimeout(function(){
                                if(!_stateBroadcast){
                                    broadcastMessage(_lastCheck);
                                }
                            },1000)
                        }
                    }
                }
            });
        },time);
}

function broadcastMessage(data){
    console.log("push notification")
    _stateBroadcast = true;
    var message = new gcm.Message();
    message.addData('occupation',data);
    sender.send(message, registrationIds, function (err, result) {
        if(err) console.error(err);
        else    console.log(result);
    });
}