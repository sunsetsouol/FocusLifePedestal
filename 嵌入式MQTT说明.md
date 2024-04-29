## 发

```
专注完成
topic：
/Focus_Life/focusEvent
发送数据示例

{"data":{
    "templateId": 8,
    "type":1
},"equipmentNumber":"123456"}

{"data":{
    "templateId": 8,
    "type":2
},"equipmentNumber":"123456"}

{"data":{
    "templateId": 8,
    "type":3
},"equipmentNumber":"123456"}

{"data":{
    "templateId": 8,
    "type":4
},"equipmentNumber":"123456"}

{"data":{
    "templateId": 8,
    "type":5
},"equipmentNumber":"123456"}
{"data":{
    "templateId": 8,
    "type":6，
    ”focusTime“：120
},"equipmentNumber":"123456"}

说明：
templateId是模板的id
type： 1：开始，2：心跳，3：暂停 4：暂停后继续 5：暂停后取消专注 6：完成
focusTime：专注时长，完成后发
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

