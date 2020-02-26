Date.prototype.format = function (format) {
    var o = {
        "m+": this.getMinutes(), //minute
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "D+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "H+": this.getHours(), //hour
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "s+": this.getSeconds(), //second
    };
    if (/(y+)/.test(format) || /(Y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(S+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getMilliseconds() + "").substr(3 - RegExp.$1.length));
    }
    for (const k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

//验证是否为数字 是:true 否:false
function checkNumber(nubmer) {
    const re = /^[0-9]+.?[0-9]*/;//判断字符串是否为数字//判断正整数/[1−9]+[0−9]∗]∗/
    return re.test(nubmer);
}

/**
 * 获取时分秒
 * time标准格式化
 * @param timestamp 时间戳
 * @returns {string} HH:mm:ss 格式时间字符串
 */
function timeFormat(timestamp) {
    if (timestamp == null || timestamp === '') {
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
    if (timestamp == null || timestamp === '') {
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
