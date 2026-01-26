from xml.etree.ElementTree import Element, SubElement, ElementTree

def write_stats_xml(stats: dict, output_path):
    root = Element(
        "erreserbak",
        urtea=str(stats["urtea"]),
        urtekoGuztizkoa=str(stats["urtekoGuztizkoa"])
    )

    for hilabetea in sorted(stats["hilabeteak"]):
        h = stats["hilabeteak"][hilabetea]

        h_elem = SubElement(
            root,
            "hilabete",
            zenbakia=str(h["hilabetea"]),
            kopurua=str(h["guztira"])
        )

        for kabina_izena, kop in h["kabinak"].items():
            SubElement(
                h_elem,
                "kabina",
                izena=kabina_izena,
                kopurua=str(kop)
            )

    tree = ElementTree(root)
    tree.write(output_path, encoding="utf-8", xml_declaration=True)
