package osu.salat23.circler.osu.domain

enum class Status(val alternativeName: String, val displayName: String, val id: Long) {
    Graveyard("graveyard", "Graveyard", -2),
    WorkInProgress("wip", "Work in progress", -1),
    Pending("loved", "Pending", 0),
    Ranked("ranked", "Ranked", 1),
    Approved("approved", "Approved", 2),
    Qualified("qualified", "Qualified", 3),
    Loved("loved", "Loved", 4);

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