import 'dart:async';

import 'package:flutter/material.dart';

import 'package:paapi/paapi.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Paapi paapi = Paapi();

  @override
  void initState() {
    super.initState();
    initPaapi();
  }

  Future<void> initPaapi() async {
    String accessKey = "*";
    String secretKey = "*";
    String host = "webservices.amazon.in";
    String region = "eu-west-1";
    String keyword = "*";
    String partnerTag = "*";
    try{
      Map<String, dynamic> result = await paapi.paapiInitiate(
        accessKey: accessKey,
        secretKey: secretKey,
        host: host,
        region: region,
        keyword: keyword,
        partnerTag: partnerTag,
      );
      print("here is initPaapi result - $result");
    } catch (e){
      print("here is initPaapi error - ${e.toString()}");
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(),
      ),
    );
  }
}
