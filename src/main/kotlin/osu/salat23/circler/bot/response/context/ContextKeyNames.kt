package osu.salat23.circler.bot.response.context

enum class ContextKeys(val value: String) {
    ClientUsername("client_username"),

    OsuServer("osu_server"),

    // user
    OsuUserId("osu_user_id"),
    OsuUserUsername("osu_user_username"),
    OsuUserIsOnline("osu_user_is_online"),
    OsuUserHasSupporter("osu_user_has_supporter"),
    OsuUserMode("osu_user_mode"),
    OsuUserAvatar("osu_user_avatar"),
    OsuUserCover("osu_user_cover"),
    OsuUserCountryName("osu_user_country_name"),
    OsuUserCountryCode("osu_user_country_code"),
    OsuUserJoinedDate("osu_user_joined_date"),
    OsuUserPerformance("osu_user_performance"),
    OsuUserGlobalRank("osu_user_global_rank"),
    OsuUserCountryRank("osu_user_country_rank"),
    OsuUserAccuracy("osu_user_accuracy"),
    OsuUserLevel("osu_user_level"),
    OsuUserLevelProgress("osu_user_level_progress"),
    OsuUserPlaycount("osu_user_playcount"),
    OsuUserPlaytime("osu_user_playtime"),
    OsuUserMaxCombo("osu_user_max_combo"),
    OsuUserRankedScore("osu_user_ranked_score"),
    OsuUserTotalScore("osu_user_total_score"),
    OsuUserTotalHits("osu_user_total_hits"),
    OsuUserHighestRank("osu_user_highest_rank"),
    OsuUserHighestRankDate("osu_user_highest_rank_date"),

    // score
    OsuScoreId("osu_score_id"),
    OsuScoreScore("osu_score_score"),
    OsuScorePerformance("osu_score_performance"),
    OsuScoreAccuracy("osu_score_accuracy"),
    OsuScoreMaxCombo("osu_score_max_combo"),
    OsuScoreDate("osu_score_date"),
    OsuScoreMode("osu_score_mode"), // todo score mode or general mode?
    OsuScoreRank("osu_score_rank"),
    OsuScoreGlobalRank("osu_score_global_rank"),
    OsuScoreCountryRank("osu_score_country_rank"),
    OsuScoreHitPerfect("osu_score_hit_perfect"),
    OsuScoreHitOk("osu_score_hit_ok"),
    OsuScoreHitMeh("osu_score_hit_meh"),
    OsuScoreHitMiss("osu_score_hit_miss"),
    OsuScoreMods("osu_score_mods"),

    // beatmap
    OsuBeatmapId("osu_beatmap_id"),
    OsuBeatmapApproachRate("osu_beatmap_approach_rate"),
    OsuBeatmapCircleSize("osu_beatmap_circle_size"),
    OsuBeatmapHpDrain("osu_beatmap_hp_drain"),
    OsuBeatmapCircleCount("osu_beatmap_circle_count"),
    OsuBeatmapSliderCount("osu_beatmap_slider_count"),
    OsuBeatmapSpinnerCount("osu_beatmap_spinner_count"),
    OsuBeatmapMaxCombo("osu_beatmap_max_combo"),
    OsuBeatmapDifficultyRating("osu_beatmap_difficulty_rating"),
    OsuBeatmapAimDifficulty("osu_beatmap_aim_difficulty"),
    OsuBeatmapSpeedDifficulty("osu_beatmap_speed_difficulty"),
    OsuBeatmapBpm("osu_beatmap_bpm"),
    OsuBeatmapSliderFactor("osu_beatmap_slider_factor"),
    OsuBeatmapOverallDifficulty("osu_beatmap_overall_difficulty"),
    OsuBeatmapFlashlightDifficulty("osu_beatmap_flashlight_difficulty"),
    OsuBeatmapMode("osu_beatmap_mode"),
    OsuBeatmapStatus("osu_beatmap_status"),
    OsuBeatmapUrl("osu_beatmap_url"),

    //beatmapset
    OsuBeatmapSetId("osu_beatmapset_id"),
    OsuBeatmapSetTitle("osu_beatmapset_title"),
    OsuBeatmapSetArtist("osu_beatmapset_artist"),
    OsuBeatmapSetCreator("osu_beatmapset_creator"),
    OsuBeatmapSetCover("osu_beatmapset_cover"),
    OsuBeatmapSetStatus("osu_beatmapset_status"),


}