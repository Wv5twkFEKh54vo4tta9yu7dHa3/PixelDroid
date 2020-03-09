package com.h.pixeldroid

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.h.pixeldroid.api.PixelfedAPI
import com.h.pixeldroid.objects.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import retrofit2.Call


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class APIUnitTest {
    @get:Rule
    public var wireMockRule = WireMockRule(8089)
    /*@Test
    fun mocked_api_publicTimeline_test(){
        /* Given */
        val mock: PixelfedAPI = mock {
            on {
                timelinePublic(null, null, null, null, null)
            } doReturn object: Call<List<Status>>{
                override fun enqueue(callback: Callback<List<Status>>) {
                    callback.onResponse(this,
                        Response.success(
                            listOf(Status(
                                "", "", "",
                                Account("", "", "", "", "", "", "", "", "", "", false, emptyList(), true, "", 5, 6, 7),
                                "", Status.Visibility.PUBLIC, false, "", emptyList(), Application("name"), emptyList(), emptyList(), emptyList(), 6, 7, 8, null, null, null, null, null, null, null, null, false, false, false, false,false)
                            ))
                        )
                }

                override fun isExecuted(): Boolean {
                    throw Error("not implemented")
                }

                override fun clone(): Call<List<Status>> {
                    throw Error("not implemented")
                }

                override fun isCanceled(): Boolean {
                    throw Error("not implemented")
                }

                override fun cancel() {
                    throw Error("not implemented")
                }

                override fun execute(): Response<List<Status>> {
                    throw Error("not implemented")
                }

                override fun request(): Request {
                    throw Error("not implemented")
                }

            }

            }
        }
        val classUnderTest = ClassUnderTest(mock)

        /* When */
        classUnderTest.doAction()

        /* Then */
        verify(mock).doSomething(any())
    }*/
    @Test
    fun api_correctly_translated_data_class() {
        stubFor(
            get(urlEqualTo("/api/v1/timelines/public"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                        """[{"id":"140364967936397312","uri":"https:\/\/pixelfed.de\/p\/Miike\/140364967936397312","url":"https:\/\/pixelfed.de\/p\/Miike\/140364967936397312","in_reply_to_id":null,"in_reply_to_account_id":null,"reblog":null,"content":"Day 8 <a href=\"https:\/\/pixelfed.de\/discover\/tags\/rotavicentina?src=hash\" title=\"#rotavicentina\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#rotavicentina<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/hiking?src=hash\" title=\"#hiking\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#hiking<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/nature?src=hash\" title=\"#nature\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#nature<\/a>","created_at":"2020-03-03T08:00:16.000000Z","emojis":[],"replies_count":0,"reblogs_count":0,"favourites_count":0,"reblogged":null,"favourited":null,"muted":null,"sensitive":false,"spoiler_text":"","visibility":"public","mentions":[],"tags":[{"name":"hiking","url":"https:\/\/pixelfed.de\/discover\/tags\/hiking"},{"name":"nature","url":"https:\/\/pixelfed.de\/discover\/tags\/nature"},{"name":"rotavicentina","url":"https:\/\/pixelfed.de\/discover\/tags\/rotavicentina"}],"card":null,"poll":null,"application":{"name":"web","website":null},"language":null,"pinned":null,"account":{"id":"115114166443970560","username":"Miike","acct":"Miike","display_name":"Miike Duart","locked":false,"created_at":"2019-12-24T15:42:35.000000Z","followers_count":14,"following_count":0,"statuses_count":71,"note":"","url":"https:\/\/pixelfed.de\/Miike","avatar":"https:\/\/pixelfed.de\/storage\/avatars\/011\/511\/416\/644\/397\/056\/0\/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","avatar_static":"https:\/\/pixelfed.de\/storage\/avatars\/011\/511\/416\/644\/397\/056\/0\/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","header":"","header_static":"","emojis":[],"moved":null,"fields":null,"bot":false,"software":"pixelfed","is_admin":false},"media_attachments":[{"id":"15888","type":"image","url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f\/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB.jpeg","remote_url":null,"preview_url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f\/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB_thumb.jpeg","text_url":null,"meta":null,"description":null}]},{"id":"140349785193451520","uri":"https:\/\/pixelfed.de\/p\/stephan\/140349785193451520","url":"https:\/\/pixelfed.de\/p\/stephan\/140349785193451520","in_reply_to_id":null,"in_reply_to_account_id":null,"reblog":null,"content":"","created_at":"2020-03-03T06:59:56.000000Z","emojis":[],"replies_count":0,"reblogs_count":0,"favourites_count":2,"reblogged":null,"favourited":null,"muted":null,"sensitive":false,"spoiler_text":"","visibility":"public","mentions":[],"tags":[],"card":null,"poll":null,"application":{"name":"web","website":null},"language":null,"pinned":null,"account":{"id":"908","username":"stephan","acct":"stephan","display_name":"Stephan","locked":false,"created_at":"2019-03-17T07:46:33.000000Z","followers_count":136,"following_count":25,"statuses_count":136,"note":"Musician, software developer &amp; hobby photographer.","url":"https:\/\/pixelfed.de\/stephan","avatar":"https:\/\/pixelfed.de\/storage\/avatars\/000\/000\/000\/908\/5nQzzsB1mkwKaUqQ9GNN_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","avatar_static":"https:\/\/pixelfed.de\/storage\/avatars\/000\/000\/000\/908\/5nQzzsB1mkwKaUqQ9GNN_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","header":"","header_static":"","emojis":[],"moved":null,"fields":null,"bot":false,"software":"pixelfed","is_admin":false},"media_attachments":[{"id":"15887","type":"image","url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/a1349f5183c2bac7d52880e8f5188df0f3b2d62a\/mvT3nYV6Wdu42Xh56Ny4VYaWU0OzbnC3wjxiqnKS.jpeg","remote_url":null,"preview_url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/a1349f5183c2bac7d52880e8f5188df0f3b2d62a\/mvT3nYV6Wdu42Xh56Ny4VYaWU0OzbnC3wjxiqnKS_thumb.jpeg","text_url":null,"meta":null,"description":null}]},{"id":"140276879742603264","uri":"https:\/\/pixelfed.de\/p\/fegrimaldi\/140276879742603264","url":"https:\/\/pixelfed.de\/p\/fegrimaldi\/140276879742603264","in_reply_to_id":null,"in_reply_to_account_id":null,"reblog":null,"content":"february 2 is the day to give flowers to Iemanj\u00e1. <a href=\"https:\/\/pixelfed.de\/discover\/tags\/salvador?src=hash\" title=\"#salvador\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#salvador<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/bahia?src=hash\" title=\"#bahia\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#bahia<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/brazil?src=hash\" title=\"#brazil\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#brazil<\/a> <a href=\"https:\/\/pixelfed.de\/discover\/tags\/iemanja?src=hash\" title=\"#iemanja\" class=\"u-url hashtag\" rel=\"external nofollow noopener\">#iemanja<\/a>","created_at":"2020-03-03T02:10:14.000000Z","emojis":[],"replies_count":0,"reblogs_count":0,"favourites_count":1,"reblogged":null,"favourited":null,"muted":null,"sensitive":false,"spoiler_text":"","visibility":"public","mentions":[],"tags":[{"name":"salvador","url":"https:\/\/pixelfed.de\/discover\/tags\/salvador"},{"name":"bahia","url":"https:\/\/pixelfed.de\/discover\/tags\/bahia"},{"name":"brazil","url":"https:\/\/pixelfed.de\/discover\/tags\/brazil"},{"name":"iemanja","url":"https:\/\/pixelfed.de\/discover\/tags\/iemanja"}],"card":null,"poll":null,"application":{"name":"web","website":null},"language":null,"pinned":null,"account":{"id":"137257212828585984","username":"fegrimaldi","acct":"fegrimaldi","display_name":"Fernanda Grimaldi","locked":false,"created_at":"2020-02-23T18:11:09.000000Z","followers_count":2,"following_count":7,"statuses_count":2,"note":"a little piece of Bahia in the fediverse.","url":"https:\/\/pixelfed.de\/fegrimaldi","avatar":"https:\/\/pixelfed.de\/storage\/avatars\/013\/725\/721\/282\/858\/598\/4\/oUPBit0TJso1xNhJfFqg_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","avatar_static":"https:\/\/pixelfed.de\/storage\/avatars\/013\/725\/721\/282\/858\/598\/4\/oUPBit0TJso1xNhJfFqg_avatar.jpeg?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35","header":"","header_static":"","emojis":[],"moved":null,"fields":null,"bot":false,"software":"pixelfed","is_admin":false},"media_attachments":[{"id":"15886","type":"image","url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/feb878b4bd60b85ac840670c6b9c809fd76b628b\/lYMrx0WF8LDqn0vTRgNJaRs7stMKtAXrgzpMrWEr.jpeg","remote_url":null,"preview_url":"https:\/\/pixelfed.de\/storage\/m\/113a3e2124a33b1f5511e531953f5ee48456e0c7\/feb878b4bd60b85ac840670c6b9c809fd76b628b\/lYMrx0WF8LDqn0vTRgNJaRs7stMKtAXrgzpMrWEr_thumb.jpeg","text_url":null,"meta":null,"description":null}]}]""" )
        ))
        val call: Call<List<Status>> = PixelfedAPI.create("http://localhost:8089")
            .timelinePublic(null, null, null, null, null)
        val statuses = call.execute().body()
        val f = statuses!![0]
        val referenceFirstStatus = Status(id="140364967936397312", uri="https://pixelfed.de/p/Miike/140364967936397312",
            created_at="2020-03-03T08:00:16.000000Z",
            account=Account(id="115114166443970560", username="Miike", acct="Miike",
                url="https://pixelfed.de/Miike", display_name="Miike Duart", note="",
                avatar="https://pixelfed.de/storage/avatars/011/511/416/644/397/056/0/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35",
                avatar_static="https://pixelfed.de/storage/avatars/011/511/416/644/397/056/0/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35",
                header="", header_static="", locked=false, emojis= emptyList(), discoverable=false,
                created_at="2019-12-24T15:42:35.000000Z", statuses_count=71, followers_count=14,
                following_count=0, moved=null, fields=null, bot=false, source=null),
                content="""Day 8 <a href="https://pixelfed.de/discover/tags/rotavicentina?src=hash" title="#rotavicentina" class="u-url hashtag" rel="external nofollow noopener">#rotavicentina</a> <a href="https://pixelfed.de/discover/tags/hiking?src=hash" title="#hiking" class="u-url hashtag" rel="external nofollow noopener">#hiking</a> <a href="https://pixelfed.de/discover/tags/nature?src=hash" title="#nature" class="u-url hashtag" rel="external nofollow noopener">#nature</a>""",
                visibility=Status.Visibility.public, sensitive=false, spoiler_text="",
                media_attachments= listOf(Attachment(id="15888", type= Attachment.AttachmentType.image, url="https://pixelfed.de/storage/m/113a3e2124a33b1f5511e531953f5ee48456e0c7/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB.jpeg",
                    preview_url="https://pixelfed.de/storage/m/113a3e2124a33b1f5511e531953f5ee48456e0c7/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB_thumb.jpeg",
                    remote_url=null, text_url=null, description=null, blurhash=null)),
            application= Application(name="web", website=null, vapid_key=null), mentions=emptyList(),
            tags= listOf(Tag(name="hiking", url="https://pixelfed.de/discover/tags/hiking", history=null), Tag(name="nature", url="https://pixelfed.de/discover/tags/nature", history=null), Tag(name="rotavicentina", url="https://pixelfed.de/discover/tags/rotavicentina", history=null)),
            emojis= emptyList(), reblogs_count=0, favourites_count=0, replies_count=0, url="https://pixelfed.de/p/Miike/140364967936397312",
            in_reply_to_id=null, in_reply_to_account=null, reblog=null, poll=null, card=null, language=null, text=null, favourited=false, reblogged=false, muted=false, bookmarked=false, pinned=false)
            assertEquals(referenceFirstStatus, f)
            //same as before, but otherwise coverage is not detected for the data classes
            assert(
                ((f.id=="140364967936397312"
                        && f.uri=="https://pixelfed.de/p/Miike/140364967936397312"
                        && f.created_at=="2020-03-03T08:00:16.000000Z"
                        && f.account.id=="115114166443970560"&& f.account.username=="Miike"&& f.account.acct=="Miike" &&
                        f.account.url=="https://pixelfed.de/Miike"&& f.account.display_name=="Miike Duart"&& f.account.note==""&&
                        f.account.avatar=="https://pixelfed.de/storage/avatars/011/511/416/644/397/056/0/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35"&&
                        f.account.avatar_static=="https://pixelfed.de/storage/avatars/011/511/416/644/397/056/0/ZhaopLJWTWJ3hsVCS5pS_avatar.png?v=d4735e3a265e16eee03f59718b9b5d03019c07d8b6c51f90da3a666eec13ab35"&&
                        f.account.header==""&& f.account.header_static=="") && !f.account.locked && f.account.emojis== emptyList<Emoji>() && !f.account.discoverable && f.account.created_at=="2019-12-24T15:42:35.000000Z" && f.account.statuses_count==71 && f.account.followers_count==14 && f.account.following_count==0 && f.account.moved==null && f.account.fields==null && !f.account.bot && f.account.source==null && f.content == """Day 8 <a href="https://pixelfed.de/discover/tags/rotavicentina?src=hash" title="#rotavicentina" class="u-url hashtag" rel="external nofollow noopener">#rotavicentina</a> <a href="https://pixelfed.de/discover/tags/hiking?src=hash" title="#hiking" class="u-url hashtag" rel="external nofollow noopener">#hiking</a> <a href="https://pixelfed.de/discover/tags/nature?src=hash" title="#nature" class="u-url hashtag" rel="external nofollow noopener">#nature</a>""" && f.visibility==Status.Visibility.public) && !f.sensitive && f.spoiler_text==""
            )
            val attchmnt = f.media_attachments[0]
            assert(attchmnt.id == "15888" && attchmnt.type == Attachment.AttachmentType.image && attchmnt.url=="https://pixelfed.de/storage/m/113a3e2124a33b1f5511e531953f5ee48456e0c7/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB.jpeg" &&
                    attchmnt.preview_url =="https://pixelfed.de/storage/m/113a3e2124a33b1f5511e531953f5ee48456e0c7/34dd6d9fb1762dac8c7ddeeaf789d2d8fa083c9f/JtjO0eAbELpgO1UZqF5ydrKbCKRVyJUM1WAaqIeB_thumb.jpeg" &&
                    attchmnt.remote_url ==null && attchmnt.text_url==null && attchmnt.description==null && attchmnt.blurhash==null )
            assert( f.application.name=="web" && f.application.website==null && f.application.vapid_key==null && f.mentions==emptyList<Mention>())

        val firstTag =f.tags[0]
        val secondTag= f.tags[1]
        val thirdTag = f.tags[2]

            assert(firstTag.name=="hiking" && firstTag.url=="https://pixelfed.de/discover/tags/hiking" && firstTag.history==null &&
            f.emojis== emptyList<Emoji>() && f.reblogs_count==0 && f.favourites_count==0&& f.replies_count==0 && f.url=="https://pixelfed.de/p/Miike/140364967936397312")
        assert(f.in_reply_to_id==null && f.in_reply_to_account==null && f.reblog==null && f.poll==null && f.card==null && f.language==null && f.text==null && !f.favourited && !f.reblogged && !f.muted && !f.bookmarked && !f.pinned)
    }

    @Test
    fun register_application(){
        stubFor(
            post(urlEqualTo("/api/v1/apps"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(""" {"id":3197,"name":"Pixeldroid","website":null,"redirect_uri":"urn:ietf:wg:oauth:2.0:oob","client_id":3197,"client_secret":"hhRwLupqUJPghKsZzpZtxNV67g5DBdPYCqW6XE3m","vapid_key":null}"""
                        )))
        val call: Call<Application> = PixelfedAPI.create("http://localhost:8089")
            .registerApplication("Pixeldroid", "urn:ietf:wg:oauth:2.0:oob", "read write follow")
        val application: Application = call.execute().body()!!
        assertEquals("3197", application.client_id)
        assertEquals("hhRwLupqUJPghKsZzpZtxNV67g5DBdPYCqW6XE3m", application.client_secret)
        assertEquals("Pixeldroid", application.name)
        assertEquals(null, application.website)
        assertEquals(null, application.vapid_key)
    }
    @Test
    fun obtainToken(){
        stubFor(
            post(urlEqualTo("/oauth/token"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{
  "access_token": "ZA-Yj3aBD8U8Cm7lKUp-lm9O9BmDgdhHzDeqsY8tlL0",
  "token_type": "Bearer",
  "scope": "read write follow push",
  "created_at": 1573979017
}"""
                        )))
        val OAUTH_SCHEME = "oauth2redirect"
        val SCOPE = "read write follow"
        val PACKAGE_ID = "com.h.pixeldroid"
        val call: Call<Token> = PixelfedAPI.create("http://localhost:8089")
            .obtainToken("123", "ssqdfqsdfqds", "$OAUTH_SCHEME://$PACKAGE_ID", SCOPE, "abc",
                "authorization_code")
        val token: Token = call.execute().body()!!
        assertEquals("ZA-Yj3aBD8U8Cm7lKUp-lm9O9BmDgdhHzDeqsY8tlL0", token.access_token)
        assertEquals("Bearer", token.token_type)
        assertEquals("read write follow push", token.scope)
        assertEquals(1573979017, token.created_at)
        assertEquals(Token("ZA-Yj3aBD8U8Cm7lKUp-lm9O9BmDgdhHzDeqsY8tlL0", "Bearer", "read write follow push",1573979017), token)


    }
}