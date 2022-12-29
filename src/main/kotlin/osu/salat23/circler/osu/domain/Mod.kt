package osu.salat23.circler.osu.domain

enum class Mod(val alternativeName: String, val id: Long) {
    // difficulty reduction
    NoFail("NF", 1),
    Easy("EZ", 2),
    HalfTime("HT", 256),

    // difficulty increase
    HardRock("HR", 16),
    SuddenDeath("SD", 32),
    Perfect("PF", 16384),
    DoubleTime("DT", 64),
    NightCore("NC", 512),
    Hidden("HD", 8),
    FadeIn("FI", 1048576),
    Flashlight("FL", 1024),

    // special
    Relax("RX", 128),
    Autopilot("AP", 8192),
    SpunOut("SO", 4096),
    Auto("AT", 2048),
    Cinema("CM", 4194304),
    ScoreV2("SV2", 536870912),
    K1("1K", 67108864),
    K2("2K", 268435456),
    K3("3K", 134217728),
    K4("4K", 32768),
    K5("5K", 65536),
    K6("6K", 131072),
    K7("7K", 262144),
    K8("8K", 524288),
    K9("9K", 16777216),
    Coop("CP", 33554432),
    KX("xK", K1.id or K2.id or K3.id or K4.id or K5.id or K6.id or K7.id or K8.id or K9.id or Coop.id),
    Mirror("MR", 1073741824),
    TargetPractice("TP", -1),
    Random("RD", -1),
    TouchDevice("TD", 4);

    companion object {
        fun fromStringArray(modsAsStrings: Array<String>): Array<Mod> {

            val mods = mutableListOf<Mod>()

            for (modString in modsAsStrings) {
                for (mod in Mod.values()) {
                    if (mod.alternativeName == modString) mods.add(mod)
                }
            }
            return mods.toTypedArray()
        }
    }
}