package kim.bifrost.rain.bilibili.model.web.bean

import java.io.Serializable

data class ReplyBean(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) : Serializable {
    data class Data(
        val assist: Int,
        val blacklist: Int,
        val cm_info: CmInfo,
        val config: Config,
        val control: Control,
        val cursor: Cursor,
        val effects: Effects,
        val folder: Folder,
        val lottery: Int,
        val note: Int,
        val replies: List<Reply>,
        val show_bvid: Boolean,
        val up_selection: UpSelection,
        val upper: Upper,
        val vote: Int
    ) : Serializable {

        data class CmInfo(
            val ads: Any
        ) : Serializable

        data class Config(
            val read_only: Boolean,
            val show_del_log: Boolean,
            val show_up_flag: Boolean,
            val showadmin: Int,
            val showentry: Int,
            val showfloor: Int,
            val showtopic: Int
        ) : Serializable

        data class Control(
            val answer_guide_android_url: String,
            val answer_guide_icon_url: String,
            val answer_guide_ios_url: String,
            val answer_guide_text: String,
            val bg_text: String,
            val child_input_text: String,
            val disable_jump_emote: Boolean,
            val giveup_input_text: String,
            val input_disable: Boolean,
            val root_input_text: String,
            val show_text: String,
            val show_type: Int,
            val web_selection: Boolean
        ) : Serializable

        data class Cursor(
            val all_count: Int,
            val is_begin: Boolean,
            val is_end: Boolean,
            val mode: Int,
            val name: String,
            val next: Int,
            val prev: Int,
            val show_type: Int,
            val support_mode: List<Int>
        ) : Serializable

        data class Effects(
            val preloading: String
        ) : Serializable

        data class Folder(
            val has_folded: Boolean,
            val is_folded: Boolean,
            val rule: String
        ) : Serializable

        data class Reply(
            val action: Int,
            val assist: Int,
            val attr: Int,
            val card_label: List<CardLabel>,
            val content: Content,
            val count: Int,
            val ctime: Int,
            val dialog: Int,
            val fansgrade: Int,
            val folder: Folder,
            val invisible: Boolean,
            val like: Int,
            val member: Member,
            val mid: Int,
            val oid: Int,
            val parent: Int,
            val parent_str: String,
            val rcount: Int,
            val replies: List<Reply>,
            val reply_control: ReplyControl,
            val root: Int,
            val root_str: String,
            val rpid: Long,
            val rpid_str: String,
            val show_follow: Boolean,
            val state: Int,
            val type: Int,
            val up_action: UpAction
        ) : Serializable {
            data class CardLabel(
                val background: String,
                val background_height: Int,
                val background_width: Int,
                val image: String,
                val jump_url: String,
                val label_color_day: String,
                val label_color_night: String,
                val rpid: Long,
                val text_color_day: String,
                val text_color_night: String,
                val text_content: String,
                val type: Int
            ) : Serializable

            data class Content(
                val device: String,
                val max_line: Int,
                val members: List<Any>,
                val message: String,
                val plat: Int
            ) : Serializable

            data class Folder(
                val has_folded: Boolean,
                val is_folded: Boolean,
                val rule: String
            ) : Serializable

            data class Member(
                val DisplayRank: String,
                val avatar: String,
                val contract_desc: String,
                val face_nft_new: Int,
                val fans_detail: Any,
                val following: Int,
                val is_contractor: Boolean,
                val is_followed: Int,
                val is_senior_member: Int,
                val level_info: LevelInfo,
                val mid: String,
                val nameplate: Nameplate,
                val official_verify: OfficialVerify,
                val pendant: Pendant,
                val rank: String,
                val sex: String,
                val sign: String,
                val uname: String,
                val user_sailing: UserSailing,
                val vip: Vip
            ) : Serializable {
                data class LevelInfo(
                    val current_exp: Int,
                    val current_level: Int,
                    val current_min: Int,
                    val next_exp: Int
                ) : Serializable

                data class Nameplate(
                    val condition: String,
                    val image: String,
                    val image_small: String,
                    val level: String,
                    val name: String,
                    val nid: Int
                ) : Serializable

                data class OfficialVerify(
                    val desc: String,
                    val type: Int
                ) : Serializable

                data class Pendant(
                    val expire: Int,
                    val image: String,
                    val image_enhance: String,
                    val image_enhance_frame: String,
                    val name: String,
                    val pid: Int
                ) : Serializable

                data class UserSailing(
                    val cardbg: Any,
                    val cardbg_with_focus: Any,
                    val pendant: Any
                ) : Serializable

                data class Vip(
                    val accessStatus: Int,
                    val avatar_subscript: Int,
                    val avatar_subscript_url: String,
                    val dueRemark: String,
                    val label: Label,
                    val nickname_color: String,
                    val themeType: Int,
                    val vipDueDate: Long,
                    val vipStatus: Int,
                    val vipStatusWarn: String,
                    val vipType: Int
                ) : Serializable {
                    data class Label(
                        val bg_color: String,
                        val bg_style: Int,
                        val border_color: String,
                        val label_theme: String,
                        val path: String,
                        val text: String,
                        val text_color: String
                    ) : Serializable
                }
            }

            data class Reply(
                val action: Int,
                val assist: Int,
                val attr: Int,
                val content: Content,
                val count: Int,
                val ctime: Int,
                val dialog: Long,
                val fansgrade: Int,
                val folder: Folder,
                val invisible: Boolean,
                val like: Int,
                val member: Member,
                val mid: Int,
                val oid: Int,
                val parent: Long,
                val parent_str: String,
                val rcount: Int,
                val replies: Any,
                val reply_control: ReplyControl,
                val root: Long,
                val root_str: String,
                val rpid: Long,
                val rpid_str: String,
                val show_follow: Boolean,
                val state: Int,
                val type: Int,
                val up_action: UpAction
            ) : Serializable {
                data class Content(
                    val device: String,
                    val max_line: Int,
                    val members: List<Member>,
                    val message: String,
                    val plat: Int
                ) : Serializable {

                    data class Member(
                        val DisplayRank: String,
                        val avatar: String,
                        val face_nft_new: Int,
                        val is_senior_member: Int,
                        val level_info: LevelInfo,
                        val mid: String,
                        val nameplate: Nameplate,
                        val official_verify: OfficialVerify,
                        val pendant: Pendant,
                        val rank: String,
                        val sex: String,
                        val sign: String,
                        val uname: String,
                        val vip: Vip
                    ) : Serializable {
                        data class LevelInfo(
                            val current_exp: Int,
                            val current_level: Int,
                            val current_min: Int,
                            val next_exp: Int
                        ) : Serializable

                        data class Nameplate(
                            val condition: String,
                            val image: String,
                            val image_small: String,
                            val level: String,
                            val name: String,
                            val nid: Int
                        ) : Serializable

                        data class OfficialVerify(
                            val desc: String,
                            val type: Int
                        ) : Serializable

                        data class Pendant(
                            val expire: Int,
                            val image: String,
                            val image_enhance: String,
                            val image_enhance_frame: String,
                            val name: String,
                            val pid: Int
                        ) : Serializable

                        data class Vip(
                            val accessStatus: Int,
                            val avatar_subscript: Int,
                            val avatar_subscript_url: String,
                            val dueRemark: String,
                            val label: Label,
                            val nickname_color: String,
                            val themeType: Int,
                            val vipDueDate: Long,
                            val vipStatus: Int,
                            val vipStatusWarn: String,
                            val vipType: Int
                        ) : Serializable {
                            data class Label(
                                val bg_color: String,
                                val bg_style: Int,
                                val border_color: String,
                                val label_theme: String,
                                val path: String,
                                val text: String,
                                val text_color: String
                            ) : Serializable
                        }
                    }
                }

                data class Folder(
                    val has_folded: Boolean,
                    val is_folded: Boolean,
                    val rule: String
                ) : Serializable

                data class Member(
                    val DisplayRank: String,
                    val avatar: String,
                    val contract_desc: String,
                    val face_nft_new: Int,
                    val fans_detail: Any,
                    val following: Int,
                    val is_contractor: Boolean,
                    val is_followed: Int,
                    val is_senior_member: Int,
                    val level_info: LevelInfo,
                    val mid: String,
                    val nameplate: Nameplate,
                    val official_verify: OfficialVerify,
                    val pendant: Pendant,
                    val rank: String,
                    val sex: String,
                    val sign: String,
                    val uname: String,
                    val user_sailing: UserSailing,
                    val vip: Vip
                ) : Serializable {
                    data class LevelInfo(
                        val current_exp: Int,
                        val current_level: Int,
                        val current_min: Int,
                        val next_exp: Int
                    ) : Serializable

                    data class Nameplate(
                        val condition: String,
                        val image: String,
                        val image_small: String,
                        val level: String,
                        val name: String,
                        val nid: Int
                    ) : Serializable

                    data class OfficialVerify(
                        val desc: String,
                        val type: Int
                    ) : Serializable

                    data class Pendant(
                        val expire: Int,
                        val image: String,
                        val image_enhance: String,
                        val image_enhance_frame: String,
                        val name: String,
                        val pid: Int
                    ) : Serializable

                    data class UserSailing(
                        val cardbg: Cardbg,
                        val cardbg_with_focus: Any,
                        val pendant: Pendant
                    ) : Serializable {
                        data class Cardbg(
                            val fan: Fan,
                            val id: Int,
                            val image: String,
                            val jump_url: String,
                            val name: String,
                            val type: String
                        ) : Serializable {
                            data class Fan(
                                val color: String,
                                val is_fan: Int,
                                val name: String,
                                val num_desc: String,
                                val number: Int
                            ) : Serializable
                        }

                        data class Pendant(
                            val id: Int,
                            val image: String,
                            val image_enhance: String,
                            val image_enhance_frame: String,
                            val jump_url: String,
                            val name: String,
                            val type: String
                        ) : Serializable
                    }

                    data class Vip(
                        val accessStatus: Int,
                        val avatar_subscript: Int,
                        val avatar_subscript_url: String,
                        val dueRemark: String,
                        val label: Label,
                        val nickname_color: String,
                        val themeType: Int,
                        val vipDueDate: Long,
                        val vipStatus: Int,
                        val vipStatusWarn: String,
                        val vipType: Int
                    ) : Serializable {
                        data class Label(
                            val bg_color: String,
                            val bg_style: Int,
                            val border_color: String,
                            val label_theme: String,
                            val path: String,
                            val text: String,
                            val text_color: String
                        ) : Serializable
                    }
                }

                data class ReplyControl(
                    val time_desc: String
                ) : Serializable

                data class UpAction(
                    val like: Boolean,
                    val reply: Boolean
                ) : Serializable
            }

            data class ReplyControl(
                val sub_reply_entry_text: String,
                val sub_reply_title_text: String,
                val time_desc: String,
                val up_reply: Boolean
            ) : Serializable

            data class UpAction(
                val like: Boolean,
                val reply: Boolean
            ) : Serializable
        }


        data class UpSelection(
            val ignore_count: Int,
            val pending_count: Int
        ) : Serializable

        data class Upper(
            val mid: Int
        ) : Serializable
    }
}