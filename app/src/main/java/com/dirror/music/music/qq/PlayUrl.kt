package com.dirror.music.music.qq

import com.dirror.music.util.MagicHttp
import com.dirror.music.util.loge
import com.google.gson.Gson
import kotlin.math.abs

object PlayUrl {

    fun getPlayUrl(songmid: String, success: (String) -> Unit) {
        // 地址
        val url = """https://u.y.qq.com/cgi-bin/musicu.fcg?g_tk=5381&loginUin=0&hostUin=0&format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&data={"req":{"module":"CDN.SrfCdnDispatchServer","method":"GetCdnDispatch","param":{"guid":"8348972662","calltype":0,"userip":""}},"req_0":{"module":"vkey.GetVkeyServer","method":"CgiGetVkey","param":{"guid":"8348972662","songmid":["${songmid}"],"songtype":[1],"uin":"0","loginflag":1,"platform":"20"}},"comm":{"uin":0,"format":"json","ct":24,"cv":0}}""".trimIndent()
        loge("请求地址：${url}")
        MagicHttp.OkHttpManager().newGet(url, {
            loge("返回的 JSON：${it}")
            // 获取 vkeyData
            val vkeyData = Gson().fromJson(it, VkeyData::class.java)
            // 获取 ip
            val ip = vkeyData.req.data.freeflowsip[0]
            // 获取 vkey
            val purl = vkeyData.req_0.data.midurlinfo[0].purl
            success.invoke(ip + purl)
        }, {

        })
    }


    data class VkeyData(
        val req: ReqData,
        val req_0: Req_0Data
    )

    data class ReqData(
        val data: Freeflowsip
    )

    data class Freeflowsip(
        val freeflowsip: ArrayList<String>
    )

    data class Req_0Data(
        val data: Midurlinfo
    )

    data class Midurlinfo(
        val midurlinfo: ArrayList<VkeyReqData>
    )

    data class VkeyReqData(
        val purl: String,
        val vkey: String
    )


}