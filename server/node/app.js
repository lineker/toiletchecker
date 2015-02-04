/**
 * Created by pteyssedre on 15-02-03.
 */
var spark = require("spark");
var gcm = require("node-gcm");

var sender = new gcm.Sender('insert Google Server API Key here');
var registrationIds = [];

var callback = function (err, body) {
    if (err) {
        console.error(err);
        return;
    }
    spark.listDevices(function (err, devices) {
        var device = devices[0];
        device.getVariable('status', function (err, data) {
            if (err) {
                console.log('An error occurred while getting attrs:', err);
            } else {
                console.log('Toilet are', (data.result == 0 ? "vacant" : "occupied"));
            }
        });
        device.subscribe('status', function (err, data) {
            if (err) {
                console.log('An error occurred while getting attrs:', err);
            } else {
                console.log('Toilet are', (data.result == 0 ? "vacant" : "occupied"));
                var message = new gcm.Message();
                message.addData('occupation',data.result);
                sender.send(message, registrationIds, function (err, result) {
                    if(err) console.error(err);
                    else    console.log(result);
                });
            }
        });
    });
};
spark.login({accessToken: 'a6c96c783d1ca41a1f36cafd0fcb101eefa04568'}, callback);
