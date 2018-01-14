package com.detect.detect.db;

/**
 * 数据库数据存储相关的常量
 *
 * @author jun.wang@raiing.com.
 * @version 1.1 2015/8/21
 */
public class DataConstant {

    /**
     * <pre>
     *     1.冷敷
     *     使用同旧版相同的结构
     *     name:ColdCompression
     *     info:不需要
     *
     *     2.服药
     *     使用新的数据结构，但兼容显示旧的事件
     *     name:pudding_medicine
     *     info:{"medicines" : ["xxxx","xxxxx"]}
     *
     *     旧版本如下：
     *     name：Medication
     *     info：{"DrugName":"xxxxxx"}
     *
     *     如果需要编辑，则删除旧版本的事件，直接新建为新版本的事件
     *
     *     3.自定义事件（新增）
     *     name：pudding_custom_event
     *     info：{"detail":"xxxxxx"}
     *
     *     布丁中的提醒
     *
     *     废弃旧版本的Alarm(不同步所以无兼容问题)，新建标准
     *
     *     1.提醒
     *     name：pudding_alarm
     *     info:{"tag" : "xxxxxx"}
     *
     *     几种固定的Tag（除此外全部为自定义提醒）：
     *     1）medicine
     *     2）cooling
     *     3）vaccine
     * </pre>
     */
    /**
     * 提醒事件药物的TAG
     */
    public static final String ALARM_EVENT_MEDICINE = "medicine";
    /**
     * 提醒事件物理降温的TAG
     */
    public static final String ALARM_EVENT_COOLING = "cooling";
    /**
     * 提醒事件疫苗接种的TAG
     */
    public static final String ALARM_EVENT_VACCINE = "vaccine";
    /**
     * 冷敷事件的名称
     */
    public static final String COLD_EVENT_NAME = "ColdCompression";
    /**
     * 就医记录事件的名称
     */
    public static final String DISEASE_EVENT_NAME = "DiseaseCompression";
    /**
     * 症状记录事件的名称
     */
    public static final String SYMPTOM_EVENT_NAME = "SymptomCompression";
    /**
     * 服药事件的名称
     */
    public static final String MEDICINE_EVENT_NAME = "pudding_medicine";

    /**
     * 服药信息存储的key名称
     */
    public static final String KEY_MEDICINE_EVENT_INFO = "medicines";

    /**
     * 温度分析模块儿分析事件后,产生的事件的名字的key
     */
    public static final String KEY_ANALYSIS_EVENT = "analysis_event";

    /**
     * 服药事件的名称,旧版，需要兼容
     */
    public static final String MEDICINE_EVENT_NAME_OLD = "Medication";

    /**
     * 服药信息存储的key名称,旧版，需要兼容
     */
    public static final String KEY_OLD_MEDICINE_EVENT_INFO = "DrugName";

    /**
     * 自定义事件的名称
     */
    public static final String CUSTOM_EVENT_NAME = "pudding_custom_event";

    /**
     * 自定义信息存储的key名称
     */
    public static final String KEY_CUSTOM_EVENT_INFO = "detail";

    /**
     * 提醒事件的名称
     */
    public static final String ALARM_EVENT_NAME = "pudding_alarm";
    /**
     * 提醒事件信息存储的key名称
     */
    public static final String KEY_ALARM_EVENT_INFO = "tag";

    /**
     * 重复提醒事件的间隔，默认86400s
     */
    public static final int REPEATE_INTERVAL = 86400;
    /**
     * 事件的类别，有两种"RVMEvent"和"ALARM"
     */
    public static final String KEY_EVENT_TYPE = "c";

    /**
     * 普通事件类型的字符串表示
     */
    public static final String VALUE_EVENT_TYPE1 = "RVMEvent";
    /**
     * 提醒事件类型的字符串表示
     */
    public static final String VALUE_EVENT_TYPE2 = "RVMAlarmEvent";

    /**
     * 事件的时间key
     */
    public static final String KEY_EVENT_TIME = "t";
    /**
     * 事件的属性key
     */
    public static final String KEY_EVENT_PROP = "p";
    /**
     * 事件的操作key
     */
    public static final String KEY_EVENT_OP = "op";
    /**
     * 事件的UUID key
     */
    public static final String KEY_EVENT_UUID = "uu";
    /**
     * 事件的内容key
     */
    public static final String KEY_EVENT_CONTENT = "ev";

    /**
     * 事件的名称key
     */
    public static final String KEY_EVENT_NAME = "name";

    /**
     * 事件的信息key
     */
    public static final String KEY_EVENT_INFO = "info";

}
