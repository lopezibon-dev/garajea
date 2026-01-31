from xml.etree.ElementTree import Element, SubElement, ElementTree

def write_stats_xml(stats: dict, output_path):
    erroa = Element(
        "erreserba_estatistikak",
        urtea=str(stats["urtea"]),
        urtekoGuztizkoa=str(stats["urtekoGuztizkoa"])
    )

    for hilabetea in sorted(stats["hilabeteak"]):
        hilabete_datuak = stats["hilabeteak"][hilabetea]

        hilabete_elem = SubElement(
            erroa,
            "hilabete",
            zenbakia=str(hilabete_datuak["hilabetea"]),
            kopurua=str(hilabete_datuak["guztira"])
        )

        for kabina_izena, kopurua in hilabete_datuak["kabinak"].items():
            SubElement(
                hilabete_elem,
                "kabina",
                izena=kabina_izena,
                kopurua=str(kopurua)
            )

    zuhaitza = ElementTree(erroa)
    zuhaitza.write(output_path, encoding="utf-8", xml_declaration=True)
