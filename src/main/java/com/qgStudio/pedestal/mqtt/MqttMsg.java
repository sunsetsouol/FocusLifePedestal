package com.qgStudio.pedestal.mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yinjunbiao
 * @version 1.0
 * @date 2024/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttMsg {
    private String equipmentNumber;
    private String data;
}
