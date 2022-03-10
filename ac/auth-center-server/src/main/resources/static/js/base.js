function resolveXhr(xhr) {
    let responseText = xhr.responseText;
    if (responseText.indexOf('后台登录') !== -1) {
        location.href = '/login';
        return;
    }
    return JSON.parse(responseText);
}