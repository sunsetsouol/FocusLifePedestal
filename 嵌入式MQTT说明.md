## 发

```
专注完成
topic：
/Focus_Life/focusEvent
发送数据示例
{"data":{
    "focusId": 8,
    "realStartTime": "2024-01-19T10:00:00",
    "focusTime": "630",
    "suspendTime": 3,
    "isCompleted": 1,
    "note": "string"
},"equipmentNumber":"123456"}
```

```
喝水一次
topic：
/Focus_Life/addWater
发送数据示例
{"data":{"number":1002},"equipmentNumber":"123456"}
```

```
获取模板
topic：
/Focus_Life/getTemplate
发送消息实例
{"equipmentNumber":"123456"}
```

```
获取今日喝水情况
topic：
/Focus_Life/getWater
发送消息示例
{"equipmentNumber":"123456"}
```

## 收

```
获取专注模板
topic:
/Focus_Life/sendTemplate/ + 设备号
收取数据示例
{"code":200,"data":[{"completion":0,"deleted":0,"focusDuration":60,"focusStartTime":"10:00:00","id":13,"missionName":"name"}],"message":"成功"}
```

```
获取喝水情况
topic：
/Focus_Life/sendWater/ + 设备号
收取数据示例
{"code":200,"data":{"intakeDate":"2024-03-19","intakeReal":4008,"intakeTarget":200},"message":"成功"}

```
