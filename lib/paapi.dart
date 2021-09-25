import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Paapi {
  static const MethodChannel _channel = const MethodChannel('paapi');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<Map<String, dynamic>> paapiInitiate({
    @required String? accessKey,
    @required String? secretKey,
    @required String? partnerTag,
    @required String? keyword,
    String? host = "webservices.amazon.in",
    String? region = "eu-west-1",
    int? itemPage = 1,
    int? itemCount = 10,
    String? searchIndex = "All",
  }) async {
    Map<String, dynamic> data = {
      "accessKey": accessKey,
      "secretKey": secretKey,
      "partnerTag": partnerTag,
      "host": host,
      "region": region,
      "keyword": keyword,
      "searchIndex": searchIndex,
      "itemCount": itemCount,
      "itemPage": itemPage,
    };
    Map<String, dynamic> _tmp = {};
    try {
      var result = await _channel.invokeMethod("paapiInitiate", data);
      _tmp = {
        "status": 200,
        "data": Map<String, dynamic>.from(result),
      }
    } catch (e) {
      _tmp = {
        "status": 400,
        "error": e.toString(),
      };
    }
    return _tmp;
  }
}
