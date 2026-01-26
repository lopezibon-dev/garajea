def build_stats_dict(urtea: int, rows):
    stats = {
        "urtea": urtea,
        "urtekoGuztizkoa": 0,
        "hilabeteak": {}
    }

    for hilabetea, kabina_izena, kopurua in rows:

        if hilabetea not in stats["hilabeteak"]:
            stats["hilabeteak"][hilabetea] = {
                "hilabetea": hilabetea,
                "guztira": 0,
                "kabinak": {}
            }

        stats["hilabeteak"][hilabetea]["kabinak"][kabina_izena] = kopurua
        stats["hilabeteak"][hilabetea]["guztira"] += kopurua

    stats["urtekoGuztizkoa"] = sum(
        h["guztira"] for h in stats["hilabeteak"].values()
    )

    return stats
