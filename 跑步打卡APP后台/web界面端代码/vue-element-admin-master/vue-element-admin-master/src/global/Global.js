/* 
    // 公共
    SUCCESS(2000, "成功"),
    UNKNOWN_ERROR(9998,"未知异常"),
    SYSTEM_ERROR(9999, "系统异常"),


    INSUFFICIENT_PERMISSION(4003, "权限不足"),

    WARN(9000, "失败"),
    REQUEST_PARAMETER_ERROR(1002, "请求参数错误"),

    // 登录
    LOGIN_EXPIRE(2001, "未登录或者登录失效"),
    LOGIN_CODE_ERROR(2002, "登录验证码错误"),
    LOGIN_ERROR(2003, "用户名不存在或密码错误"),
    LOGIN_USER_STATUS_ERROR(2004, "用户状态不正确"),
    LOGOUT_ERROR(2005, "退出失败，token不存在"),
    LOGIN_USER_NOT_EXIST(2006, "该用户不存在"),
    LOGIN_USER_EXIST(2007, "该用户已存在"); */



// 状态码
const statusCode = {
    // 公共
    SUCCESS_2000: 2000,
    UNKNOWN_ERROR_9998: 9998,
    SYSTEM_ERROR_9999: 9999,

    INSUFFICIENT_PERMISSION_4003: 4003,

    WARN_9000: 9000,
    REQUEST__PARAMETER_ERROR_1002: 1002,
    // 登录
    LOGIN_EXPIRE_2001: 2001,
    LOGIN_CODE_ERROR_2002: 2002,
    LOGIN_ERROR_2003: 2003,
    LOGIN_USER_STATUS_ERROR_2004: 2004,
    LOGOUT_ERROR_2005: 2005,
    LOGIN_USER_NOT_EXIST_2006: 2006,
    LOGIN_USER_EXIST_2007: 2007,

    // token
    ILLEGAL_TOKEN_50008: 50008,
    OTHER_CLIENTS_LOGGED_IN_50012: 50012,
    TOKEN_EXPIRED_50014: 50014,

    // HTTP常见状态码
    NOT_FIND_404: 404,
    SERVER_UNKNOWN_ERROR_500: 500,
    SERVICE_UNAVAILABLE_503: 503
}

// 状态码对应得中文意思
const statusCodeMean = {
    // 公共
    '_2000': '成功',
    '_9998': '未知异常',
    '_9999': '系统异常',

    '_4003': '权限不足',

    '_9000': '失败',
    '_1002': '请求参数错误',
    // 登录
    '_2001': '未登录或者登录失效',
    '_2002': '登录验证码错误',
    '_2003': '用户名不存在或密码错误',
    '_2004': '用户状态不正确',
    '_2005': '退出失败，token不存在',
    '_2006': '该用户不存在',
    '_2007': '该用户已存在',

    // token
    '_50008': '非法token',
    '_50012': '其他客户端登录',
    '_50014': 'token过期',

    // HTTP常见状态码
    '_404': '请求失败，请求的资源没有找到或者是不存在',
    '_500': '服务器遇到未知的错误，导致无法完成客户端当前的请求',
    '_503': '服务不可用'
}

export default {
    statusCode,
    statusCodeMean
}

