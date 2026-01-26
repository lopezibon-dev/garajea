import logging
from pathlib import Path
from lxml import etree

logger = logging.getLogger(__name__)

def apply_xslt(xml_path: Path, xslt_path: Path, output_path: Path):
    """
    XML bati XSLT orria ezarri eta HTML sortu
    """

    if not xml_path.exists():
        raise FileNotFoundError(f"XML ez da existitzen: {xml_path}")

    if not xslt_path.exists():
        raise FileNotFoundError(f"XSLT ez da existitzen: {xslt_path}")

    logger.info("XSLT aplikatzen")
    logger.info("  XML  : %s", xml_path)
    logger.info("  XSLT : %s", xslt_path)
    logger.info("  EMAITZA : %s", output_path)

    # XML kargatu
    xml_tree = etree.parse(str(xml_path))

    # XSLT kargatu
    xslt_tree = etree.parse(str(xslt_path))
    transform = etree.XSLT(xslt_tree)

    # Transformazioa ezarri
    result_tree = transform(xml_tree)

    # Emaitza gorde
    output_path.parent.mkdir(parents=True, exist_ok=True)
    output_path.write_bytes(
        etree.tostring(
            result_tree,
            pretty_print=True,
            encoding="utf-8",
            doctype="<!DOCTYPE html>"
        )
    )

    logger.info("HTML behar bezala sortua")
