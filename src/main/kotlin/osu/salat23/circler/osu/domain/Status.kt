package osu.salat23.circler.osu.domain

enum class Status(val alternativeName: String, val id: Long) {
    Graveyard("graveyard", -2),
    WorkInProgress("wip", -1),
    Pending("loved", 0),
    Ranked("ranked", 1),
    Approved("approved", 2),
    Qualified("qualified", 3),
    Loved("loved", 4);

    companion object {
        fun from(id: Long): Status {
            for (value in Status.values()) {
                if (value.id == id) return value;
            }
            return Graveyard
        }

        fun from(name: String): Status {
            for (value in Status.values()) {
                if (value.alternativeName == name) return value;
            }
            return Graveyard
        }
    }
}