import logging
from pathlib import Path
from garajea_paths import BASE_DIR, DATA_DIR
import sys

from db_lortu_datuak import fetch_erreserba_stats
from sortu_estatistikak import build_stats_dict
from sortu_xml import write_stats_xml
from transform_xslt import apply_xslt_html

# Logging
LOG_FILE = r"C:\logs\garajea_python.log"
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [PYTHON] %(levelname)s: %(message)s',
    handlers=[logging.FileHandler(LOG_FILE, encoding="utf-8")]
)

logger = logging.getLogger(__name__)

def ensure_dirs():
    # data direkorioa sortu, behar izanez gero
    DATA_DIR.mkdir(exist_ok=True)

def main():
    # Argumentuen balidazioa
    if len(sys.argv) != 2:
        print("Erabilera:")
        print("sortu_txostenak.py <urtea>")
        sys.exit(1)

    urtea = int(sys.argv[1])

    logger.info("%s urterako estatistikak sortzen", urtea)

    # Datu-basetik datu-errenkadak lortu
    datu_errenkadak = fetch_erreserba_stats(urtea)

    # Datu-errenkadak zuhaitz itxura duen dictionary egitura komplexu batean
    # gorde, eta zenbait datu agregatuak kalkulatu
    stats = build_stats_dict(urtea, datu_errenkadak)

    # Dictionary egitura xml liburutegiaren bidez zuhaitz batean bihurtu
    # eta zuhaitz hori XML fitxategian serializatu
    xml_file  = DATA_DIR / f"stats_erreserbak_{urtea}.xml"
    write_stats_xml(stats, xml_file)

    logger.info("XML sortua: %s", xml_file)

    # XML fitxategia XSLT bidez HTML txostenetan bihurtu
    # HTML Txostena : hilabeteka
    apply_xslt_html(
        xml_file,
        BASE_DIR / "scripts" / "stats_erreserbak_hilabeteka.xsl",
        DATA_DIR / f"txostena_erreserbak_hilabeteka_{urtea}.html"
    )

    # HTML Txostena : kabinaka
    apply_xslt_html(
        xml_file,
        BASE_DIR / "scripts" / "stats_erreserbak_kabinaka.xsl",
        DATA_DIR / f"txostena_erreserbak_kabinaka_{urtea}.html"
    )

    logger.info("Txostenak behar bezala sortu dira")

if __name__ == "__main__":
    ensure_dirs()
    main()
