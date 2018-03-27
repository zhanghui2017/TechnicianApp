package com.gengli.technician.http;

public class ServicePort {
    //测试地址
//    private static final String API = "http://gengli.test.dxkj.com/api/test/";
    //正式地址
    private static final String API = "http://gengli.test.dxkj.com/api/";
    //假数据地址
//    private static final String API = "http://gengli.test.dxkj.com/api/test/index/data?qt=";
    /**
     * 客户端启动
     */
    public static String GET_CLIENT_RUN = API + "client/run/jsd";
    /**
     * 登录
     */
    public static String ACCOUNT_LOGIN_WORKER = API + "account/login_worker";
    /**
     * 退出登录
     */
    public static String ACCOUNT_LOGOUT = API + "account/logout";
    /**
     * 获取验证码
     */
    public static String DATA_VERIFY = API + "data/verify";
    /**
     * 获取图形验证码
     */
    public static String DATA_CAPTCHA = API + "data/captcha";
    /**
     * 收藏列表
     */
    public static String FAV_LISTS = API + "fav/lists";
    /**
     * 添加收藏
     */
    public static String FAV_ADD = API + "fav/add";
    /**
     * 删除收藏
     */
    public static String FAV_REMOVE = API + "fav/remove";
    /**
     * 获取用户的行程列表
     */
    public static String TRIP_LISTS = API + "trip/lists";
    /**
     * 添加行程
     */
    public static String TRIP_ADD = API + "trip/add";
//    /**
//     * 忘记密码
//     */
//    public static String ACCOUNT_FORGET = API + "account/forget";
    /**
     * 绑定手机号
     */
    public static String ACCOUNT_BIND = API + "account/bind";
//    /**
//     * 注册
//     */
//    public static String ACCOUNT_REGISTER = API + "account/register";

    /**
     * 获取浏览记录列表
     */
    public static String LOG_ARCHIVE = API + "log/archive";

    /**
     * 浏览记录删除，清空
     */
    public static String LOG_ARCHIVE_REMOVE = API + "log/archive_remove";

    /**
     * 指南文章列表
     */
    public static String ARCHIVE_LISTS = API + "archive/lists";

    /**
     * 文章详情页
     */
    public static String ARCHIVE_DETAIL = API + "archive/detail";

    /**
     * 获取热词列表
     */
    public static String ARCHIVE_HOTWORDS = API + "archive/hotwords";

    /**
     * 获取报修单列表
     */
    public static String REPAIR_LISTS = API + "repair/lists";

    /**
     * 报修单接单
     */
    public static String REPAIR_CONFIRM = API + "repair/confirm";
    /**
     * 报修单签到
     */
    public static String REPAIR_SIGN = API + "repair/sign";

    /**
     * 报修单签到
     */
    public static String REPAIR_PROBLEM_ADD = API + "repair/problem_add";

    /**
     * 配件库存
     */
    public static String PART_LISTS = API + "part/lists";

    /**
     * 申请配件
     */
    public static String REPAIR_PART_ADD = API + "repair/part_add";

    /**
     * 配件详情
     */
    public static String REPAIR_DETAIL = API + "repair/detail";

    /**
     * 提交订单
     */
    public static String REPAIR_FINISH = API + "repair/finish";

    /**
     * 上传头像
     */
    public static String ACCOUNT_MODIFY = API + "account/modify";

    /**
     * 消息
     */
    public static String MSG_LISTS = API + "msg/lists";

    /**
     * 修改密码
     */
    public static String ACCOUNT_REPASS = API + "account/repass";

    /**
     * 检查更细
     */
    public static String CLIENT_UPDATE = API + "client/update";


    /**
     * 操作手添加记录
     */
    public static String OPERATOR_ADD = API + "operator/add";

    /**
     * 操作手记录表
     */
    public static String OPERATOR_LISTS = API + "operator/lists";

    public static String EVALUATE_ADD = API + "evaluate/add";
    public static String EVALUATE_LISTS = API + "evaluate/lists";
    public static String EVALUATE_DETAIL = API + "evaluate/detail";

}
