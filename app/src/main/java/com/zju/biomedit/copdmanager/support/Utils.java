package com.zju.biomedit.copdmanager.support;

import com.zju.biomedit.copdmanager.activity.SixMinuteWalkTestActivity;

/**
 * Created by wangzheyu on 2017/7/17.
 */

public class Utils {

    public static final String MESSAGE_UPLOADING = "数据上传中，请稍后";
    public static final String MESSAGE_UPLOAD_FAIL = "数据上传失败，请稍后重试";
    public static final String MESSAGE_UPLOAD_SUCCESS = "数据上传成功！";
    public static final String MESSAGE_GET_MEDICINE_LIST = "正在获取今日服药记录";
    public static final String MESSAGE_GET_APPOINTMENT_LIST = "正在获取可预约时间段";
    public static final String MESSAGE_SEND_APPOINTMENT = "正在提交预约申请";
    public static final String MESSAGE_GET_APPOINTMENT_HISTORY = "正在查询预约记录";
    public static final String MESSAGE_CANCEL_APPOINTMENT = "正在取消预约";
    public static final String MESSAGE_GET_SCALE_LIST = "正在获取量表填写记录";
    public static final String MESSAGE_GET_KNO_LIST = "正在获取健康知识";
    public static final String MESSAGE_GET_COLLECT_KNO_LIST = "正在获取收藏列表";
    public static final String MESSAGE_GET_KNO_CONTENT = "知识加载中";
    public static final String MESSAGE_GET_CONTENT = "内容加载中";
    public static final String MESSAGE_VALIDATE = "正在验证中";
    public static final String MESSAGE_REGISTER = "正在提交注册信息";
    public static final String MESSAGE_LOGIN = "正在登录中";
    public static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录

    /**
     * StartActivity RequestCode & ResultCode
     */
    public static final int REQUESTCODE_FOR_REGIST_ACTIVITY = 101;
    public static final int REQUESTCODE_FOR_PERSONAL_ACTIVITY = 102;
    public static final int RESULTCODE_FROM_REGIST_ACTIVITY = 901;
    public static final int RESULTCODE_FROM_PERSONAL_ACTIVITY = 902;


    /**
     * 服务端返回值定义
     */
    public static final int SERVICE_RETURN_SUCCEED = 200; //成功 //有版本更新
    public static final int SERVICE_RETURN_INFO_ALREADY_BOUND = 230; //信息已经被绑定
    public static final int SERVICE_RETURN_ACCOUNT_DISABLED = 240; //用户已禁用
    public static final int SERVICE_RETURN_PATIENTID_NOTEXIST = 246; //用户不存在
    public static final int SERVICE_RETURN_PATIENTNAME_NOTMATCH = 247; //用户姓名不匹配
    public static final int SERVICE_RETURN_ALREADYREGIST = 248; //已经注册
    public static final int SERVICE_RETURN_FAILED = 254; //失败 //无版本更新
    public static final int SERVICE_RETURN_ERROR = 255; //异常

    /**
     * 异常情况
     */
    public static final String NETWORK_DISABLED = "当前网络不可用";
    public static final String ERROR_MESSAGE = "发生错误";

    /**
     * Preferences
     */
    public static final String PREFERENCES_SAVE_LOGIN_INFO = "todoapp.preference.save.login.info";
    public static final String PREFERENCES_SAVE_LOGIN_INFO_KEY_ACCOUNT = "LOGIN_ACCOUNT_KEY";
    public static final String PREFERENCES_SAVE_LOGIN_INFO_KEY_PASSWORD = "LOGIN_PASSWORD_KEY";
    public static final String PREFERENCES_SAVE_LOGIN_INFO_KEY_SAVESTATE = "LOGIN_SAVESTATE_KEY";
    public static final String PREFERENCES_SAVE_STEP_INFO = "todoapp.preference.save.step.info";
    public static final String PREFERENCES_SAVE_STEP_INFO_KEY_TOTAL_STEP = "STEP_TOTAL_STEP_KEY";
    public static final String PREFERENCES_SAVE_STEP_INFO_KEY_SAVED_DATE = "STEP_SAVED_DATE_KEY";
    public static final String PREFERENCES_SAVE_STEP_INFO_KEY_PEDOMETER_ENABLE = "STEP_PEDOMETER_ENABLE_KEY";

    /**
     * 接口相关
     */
    public static final String APP_DOWNLOAD_URL = "http://www.zjubiomedit.com:18909/products/copd.apk";
    public static final String BASE_URL = "http://120.27.141.50:18909/COPDService.svc/";
    public static final String WAP_LOGIN = "/WapLogin";
    public static final String VALIDATE_REGISTER = "/ValidateRegister";
    public static final String WAP_REGIST_WITH_PATIENT_INFO = "/WapRegistWithPatientInfo";
    public static final String GET_LAST_GENERIC_RECORDS = "/GetLastGenericRecords";
    public static final String GET_GENERIC_RECORDS = "/GetGenericRecords";
    public static final String COMMIT_GENERIC_RECORD = "/CommitGenericRecord";
    public static final String CHECK_APPUPDATE_METHOD = "/CheckAppUpdate";

    /**
     * 请求数据类型
     */
    public static final int CAT = 1;
    public static final int GAD = 2;
    public static final int PHQ = 3;
    public static final int PEF = 4;
    public static final int MEDICATION = 5;
    public static final int DISCOMFORT = 6;
    public static final int EVALUATION = 7;
    public static final int STEP = 8;
    public static final int SixMWD =9;

    /**
     * register steps
     */
    public static final String REGISTER_STEP_VALIDATE = "REGISTER_STEP_VALIDATE";
    public static final String REGISTER_STEP_IDENTITY_CARD = "REGISTER_STEP_IDENTITY_CARD";
    public static final String REGISTER_STEP_BIRTHDAY = "REGISTER_STEP_BIRTHDAY";
    public static final String REGISTER_STEP_SEX = "REGISTER_STEP_SEX";
    public static final String REGISTER_STEP_HEIGHT = "REGISTER_STEP_HEIGHT";
    public static final String REGISTER_STEP_WEIGHT = "REGISTER_STEP_WEIGHT";
    public static final String REGISTER_STEP_EDUCATION = "REGISTER_STEP_EDUCATION";
    public static final String REGISTER_STEP_PROFESSION = "REGISTER_STEP_PROFESSION";
    public static final String REGISTER_STEP_CONTACT = "REGISTER_STEP_CONTACT";
    public static final String REGISTER_STEP_FINISH = "REGISTER_STEP_FINISH";
    public static final String REGISTER_STEP_ADDRESS = "REGISTER_STEP_ADDRESS";
    public static final String REGISTER_STEP_SMOKE = "REGISTER_STEP_SMOKE";

    /**
     * 用户统计相关
     */
    public static final String P_LOGIN = "p_login";
    public static final String P_REGIST = "p_regist";
    public static final String P_HOME = "p_home";
    public static final String P_CLASS = "p_class";
    public static final String P_USER = "p_user";
    public static final String P_SCALE_LIST = "p_scale_list";

    public static final String P_6MWD="p_6MWD";
    public static final String P_SCALE_CAT = "p_scale_cat";
    public static final String P_SCALE_DEPRESS = "p_scale_depress";
    public static final String P_SCALE_ANXIETY = "p_scale_anxiety";
    public static final String P_PEF = "p_pef";
    public static final String P_MEDICINE = "p_medicine";
    public static final String P_DISCOMFORT = "p_discomfort";
    public static final String P_MEDICINE_HISTORY = "p_medicine_history";
    public static final String P_CLASS_KNOWLEDGE = "p_class_knowledge";
    public static final String P_CLASS_VIDEO = "p_class_video";
    public static final String P_USER_INFO = "p_user_info";
    public static final String P_USER_FAVOURITE = "p_user_favourite";
    public static final String P_USER_REMIND = "p_user_remind";
    public static final String P_REMIND_MEDICINE = "p_remind_medicine";
    public static final String P_REMIND_CAT = "p_remind_cat";
    public static final String P_REMIND_PEF = "p_remind_pef";
    public static final String P_REMIND_DEPRESS = "p_remind_depress";
    public static final String P_REMIND_ANXIETY = "p_remind_anxiety";
    public static final String P_USER_FEEDBACK = "p_user_feedback";
    public static final String P_EVALUATION = "p_evaluation";
    public static final String E_KNOWLEDGE = "e_knowledge";
    public static final String E_VIDEO_MEDICINE = "e_video_medicine";
    public static final String E_VIDEO_EXPERT = "e_video_expert";
    public static final String E_REMIND_MEDICINE = "e_remind_medicine";
    public static final String E_REMIND_CAT = "e_remind_cat";
    public static final String E_REMIND_PEF = "e_remind_pef";
    public static final String E_REMIND_DEPRESS = "e_remind_depress";
    public static final String E_REMIND_ANXIETY = "e_remind_anxiety";

    /**
     * Intent putExtra Key
     */
    public static final String INTENT_EXTRA_KEY_PATIENT = "INTENT_EXTRA_KEY_PATIENT";
    public static final String INTENT_EXTRA_KEY_LOCAL_STEP = "INTENT_EXTRA_KEY_LOCAL_STEP";
    public static final String INTENT_EXTRA_KEY_FEEDBACK_BP = "INTENT_EXTRA_KEY_FEEDBACK_BP";
    public static final String INTENT_EXTRA_KEY_QUESTIONNAIRE_ID = "INTENT_EXTRA_KEY_QUESTIONNAIRE_ID";
    public static final String INTENT_EXTRA_KEY_APPOINTMENT_TOKEN = "INTENT_EXTRA_KEY_APPOINTMENT_TOKEN";

    /**
     * 自定义Action
     */
    public static final String APP_UPDATE_SERVICE_RECEIVE_ACTION = "copdmanager.app.update.service.action.RECEIVER";
    public static final String APP_UPDATE_ACTIVITY_RECEIVE_ACTION = "copdmanager.app.update.activity.action.RECEIVER";
    public static final String LOCAL_STEP_CHANGED_ACTION = "copdmanager.local.step.action.STEP_CHANGED";

    /**
     * 自动更新相关
     */
    public static final String BTN_TEXT_LATER = "稍后再说";
    public static final String BTN_TEXT_EXIT = "退出应用";
    public static final String INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE = "INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_CODE";
    public static final String INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_INFO = "INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_INFO";
    public static final String INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_FORCE = "INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_VERSION_FORCE";
    public static final String INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_DOWNLOAD_PROGRESS = "INTENT_EXTRA_KEY_AUTO_UPDATE_SERVICE_TO_ACTIVITY_MSG_DOWNLOAD_PROGRESS";
    public static final int MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_NO_UPDATE = -1;
    public static final int MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_NEW_VERSION = 0;
    public static final int MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_DOWNLOADING = 1;
    public static final int MSG_CODE_AUTO_UPDATE_SERVICE_TO_ACTIVITY_FINISH = 2;
    public static final String INTENT_EXTRA_KEY_AUTO_UPDATE_ACTIVITY_TO_SERVICE_MSG_CODE = "INTENT_EXTRA_KEY_AUTO_UPDATE_ACTIVITY_TO_SERVICE_MSG_CODE";
    public static final int MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_UPDATE_LATER = -1;
    public static final int MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_UPDATE_NOW = 0;
    public static final int MSG_CODE_AUTO_UPDATE_ACTIVITY_TO_SERVICE_TRYCHECK_DOWNLOAD = 1;

    /**
     * APP Path
     * Dir and File Name
     */

    //public in SDCard
    public static final String SHARE_DIR_DOWNLOAD = "copd/download/";
    public static final String SHARE_FILE_DOWNLOAD_APK = "copd.apk";

    /**
     * PendingIntent ReuestCode
     * 1~99 保留 （提醒、设置等使用）
     */
    public static final int PENDINGINTENT_REQUESTCODE_NOTIFICATION_DOWNLOADAPP = 100; //下载app的通知

    /**
     * Notification ID
     * 与PendingIntent RequestCode对应
     */
    public static final int NOTIFICATION_ID_DOWNLOADAPP = 100;
    public static final int NOTIFICATION_ID_STEP_CHANGED = 101;

    /**
     * 自定义药物相关
     */
    public static final String CUSTOM_DRUG = "自定义用药";

    /**
     * 门诊预约相关
     */
    public static final String APPOINTMENT_URL = "http://119.60.4.6//trakcarelive/trak/web/http%3A//10.0.11.35/trakcarelive/trak/web//DHCMobileApp.DHCAppMobileService.cls";
    public static final String DOCTOR_NAME = "陈娟";
    public static final String GET_DOC = "GetOPDoc";
    public static final String APP_SUBMIT = "OPAppSubmit";
    public static final String GET_APP_HISTORY = "QueryOrder";
    public static final String CANCEL_APP = "CancelOrder";

    /**
     * 不适记录提示相关
     */
    public static final String INFLAMMATION1 = "咳嗽";
    public static final String INFLAMMATION1_HINT = "若出现单纯咳嗽，咽部有痒感、异物感，考虑气道高反应，可吸入布地奈德福莫特罗，或口服茶碱类药物、扑尔敏等，若前驱有受凉史，可加用金莲清热颗粒、莲花清瘟胶囊、四季抗病毒口服液等抗病毒药物。";
    public static final String INFLAMMATION2 = "黄痰";
    public static final String INFLAMMATION2_HINT = "若咳嗽合并咳黄痰，考虑感染，可口服抗生素（患者不过敏的抗生素），若3天后症状无改善，可根据症状就诊门/急诊。";
    public static final String INFLAMMATION3 = "发烧";
    public static final String INFLAMMATION3_HINT = "若前驱有受凉史，可加用金莲清热颗粒、莲花清瘟胶囊、四季抗病毒口服液等抗病毒药物，若合并咳嗽、咳痰，可口服抗生素（患者不过敏的抗生素）。";
    public static final String INFLAMMATION4 = "血痰";
    public static final String INFLAMMATION4_HINT = "请确认是否口服能引起出血的相关药物：如阿司匹林、丹参片、硫酸氢氯吡格雷片、华法林、低分子肝素等，若有，可暂停药并就诊慢病门诊调整药物治疗；若无口服上述药物，单纯痰中带血丝，量较少，可暂观察；若痰血较多，且色鲜红，可暂口服云南白药，并就诊门/急诊。";
    public static final String LUNG1 = "气短";
    public static final String LUNG2 = "喘息";
    public static final String LUNG3 = "活动后气短加重";
    public static final String LUNG_HINT = "建议您吸入短效β-2受体激动剂（特布他林，沙丁胺醇）或联合短效抗胆碱能药物（异丙托溴铵），若症状缓解不明显或持续加重，建议您预约慢病门诊，进一步指导诊治。";
    public static final String HEART1 = "腿肿";
    public static final String HEART1_HINT = "腿肿多长时间？以前有无腿肿的病史，除了腿肿其他部位有无水肿，最近有没有病情加重，有没有高血压，糖尿病，甲亢，肾脏疾病，若没有那您需要做一下生化检查，看蛋白低不低。";
    public static final String HEART2 = "腹胀";
    public static final String HEART2_HINT = "腹胀多久了，腹胀与吃饭有没有关系，最近有没有胃病：如胃疼等症状，吃没吃什么药物，最近吃饭好不好，若吃饭不好可以先吃点开胃的药，如果还没有好，我们需要检查一下什么原因，如果其他胃肠道没有问题，那我们这个病也会出现因循环不好胃肠道瘀血腹胀，就需要积极治疗肺上疾病啦";
    public static final String HEART3 = "反酸水";
    public static final String HEART3_HINT = "最近才开始出现这种症状吗？有没有胃溃疡病、胃食管反流的病史，去消化科看过吗？如果没有我们可以先口服点药，如奥美拉唑，雷贝拉唑，如果能好就可以，好不了我们需要做胃镜检查一下，而且出现返酸症状，建议您吃饭后半小时不要睡觉，少食多餐，少喝咖啡和糖类";
    public static final String HEART4 = "消瘦";
    public static final String HEART4_HINT = "最近多长时间出现的呢，多久瘦了有多少斤？最近吃饭有没有减少？有没有胃病，最近肺上的病有没有加重，觉得最近除了瘦还有其他症状吗？如果短期内瘦的很明显，还是建议您早点去医院看病。";
    public static final String BREATH1 = "迷糊";
    public static final String BREATH2 = "嗜睡";
    public static final String BREATH_HINT = "请确认是否口服促进睡眠药物，如扑尔敏、艾司唑仑、安定等，若有可停服，并确认是否存在睡眠颠倒，若无上述情况仍感嗜睡，建议就诊门急诊。";

    /**
     * 天气相关
     */
    public static final String WEATHER_NOW_URL = "https://free-api.heweather.com/s6/weather/now";
    public static final String WEATHER_AIR_URL = "https://free-api.heweather.com/s6/air/now";
    public static final String IP_URL = "http://pv.sohu.com/cityjson?ie=utf-8";

    /**
     * 知识相关
     */
    public static final String IF_KNO_READ_URL = "http://120.27.141.50:8080/copd/message/recordIfRead?value=1";
    public static final String GET_KNO_INFO_URL = "http://120.27.141.50:8080/copd/message/getKnoHistory?knoId=";
    public static final String KNO_TIME_URL = "http://120.27.141.50:8080/copd/message/recordReadTime?patientId=";
    public static final String KNO_COLLECT_URL = "http://120.27.141.50/copd/message/recordFavorite?patientId=";
    public static final String IF_KNO_COLLECT_URL = "http://120.27.141.50:8080/copd/message/getUserReadHistory?patientId=";
    public static final String NEW_USER_URL = "http://120.27.141.50:8080/copd/message/updateNewUserKnowledge?newUserId=";

}
