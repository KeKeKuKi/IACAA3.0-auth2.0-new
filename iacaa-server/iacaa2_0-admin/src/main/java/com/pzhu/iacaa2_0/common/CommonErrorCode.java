package com.pzhu.iacaa2_0.common;

public enum CommonErrorCode {
    //RPC层调用错误码
    DB_SERVICE_OK(20100,"服务正常")
    ,DB_SERVICE_DBDAO_ERROR(20104,"返回数据库的具体异常信息")

    ,DB_SERVICE_SPEAKER_NOT_EXISTED(20108,"数据库中没有要查询的speaker")
    ,DB_SERVICE_SPEAKER_HAVE_NOT_VP(20109,"speaker中没有对应的voiceprint")

    ,DB_SERVICE_UNKNOWN_ERROR(20101,"未知异常")
    ,DB_SERVICE_AGENT_ERROR	(20102,	"DBServiceAgent异常")
    ,DB_SERVICE_NETWORK_ERROR (20103,"网络异常")

    ,DB_SERVICE_INVALID_FUNCTION(20105,	"方法名不存在")
    ,DB_SERVICE_INVALID_PARAMETER(20106,"方法参数错误")
    ,DB_SERVICE_FUNCTION_NO_ACCESS(20107,"对此方法无访问权限");

    private String msg;
    private int code;

    private CommonErrorCode(int code,String msg)
    {
        this.code=code;
        this.msg=msg;
    }

    public String getMsg()
    {
        return this.msg;
    }
    public int getCode() {
        return this.code;
    }
}
