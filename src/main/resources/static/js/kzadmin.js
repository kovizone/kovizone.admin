
/**
 * datetime标准格式化
 * @param timestamp 时间戳
 * @returns {string} yyyy-MM-ss HH:mm:ss 格式时间字符串
 */
function datetimeFormat(timestamp) {
    if (timestamp == null || timestamp == '') {
        return "";
    }
    var date = new Date(timestamp);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    var day = date.getDate();
    if (day < 10) {
        day = "0" + day;
    }
    var hours = date.getHours();
    if (hours < 10) {
        hours = "0" + hours;
    }
    var minutes = date.getMinutes();
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    var seconds = date.getSeconds();
    if (seconds < 10) {
        seconds = "0" + seconds;
    }

    return year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;
}

//验证是否为数字 是:true 否:false
function checkNumber(nubmer) {
    var re = /^[0-9]+.?[0-9]*/;//判断字符串是否为数字//判断正整数/[1−9]+[0−9]∗]∗/
    if (!re.test(nubmer)) {
        return false;
    }
    return  true;
}

/**
 * 获取时分秒
 * time标准格式化
 * @param timestamp 时间戳
 * @returns {string} HH:mm:ss 格式时间字符串
 */
function timeFormat(timestamp) {
    if (timestamp == null || timestamp == '') {
        return "";
    }
    var date = new Date(timestamp);
    var hours = date.getHours();
    if (hours < 10) {
        hours = "0" + hours;
    }
    var minutes = date.getMinutes();
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    var seconds = date.getSeconds();
    if (seconds < 10) {
        seconds = "0" + seconds;
    }

    return hours + ":" + minutes + ":" + seconds;
}

/**
 * date标准格式化
 * @param timestamp 时间戳
 * @returns {string} yyyy-MM-ss 格式时间字符串
 */
function dateFormat(timestamp) {
    if (timestamp == null || timestamp == '') {
        return "";
    }
    var date = new Date(timestamp);
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    if (month < 10) {
        month = "0" + month;
    }
    var day = date.getDate();
    if (day < 10) {
        day = "0" + day;
    }

    return year + "-" + month + "-" + day;
}

/**
 * form对象提取数据转对象
 * @param form
 */
function serializeObject(form) {
    var o = {};
    $.each(form.serializeArray(), function (index) {
        if (o[this['name']]) {
            o[this['name']] = o[this['name']] + ";" + this['value'];
        } else {
            o[this['name']] = this['value'];
        }
    });
    return o;
}

/**
 * 睡眠
 * @param millis 毫秒
 */
function sleep(millis) {
    var now = new Date();
    var exitTime = now.getTime() + millis;
    while (true) {
        now = new Date();
        if (now.getTime() > exitTime)
            return;
    }
}
