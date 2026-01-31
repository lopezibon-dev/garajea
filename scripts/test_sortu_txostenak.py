import logging
import sys
from pathlib import Path

from transform_xslt import apply_xslt_html

# Logging sinplea
LOG_FILE = r"C:\logs\garajea_python.log"
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s [PYTHON] %(levelname)s: %(message)s',
    handlers=[logging.FileHandler(LOG_FILE, encoding="utf-8")]
)

logger = logging.getLogger(__name__)

BASE_DIR = Path(__file__).resolve().parent.parent
DATA_DIR = BASE_DIR / "data"

def ensure_dirs():
    # data direkorioa sortu, behar izanez gero
    DATA_DIR.mkdir(exist_ok=True)

def main():
    # Argumentuen balidazioa
    if len(sys.argv) != 3:
        print("Erabilera:")
        print("  test_sortu_txostenak.py <xml_fitxategia> <urtea>")
        sys.exit(1)

    # XML fitxategiaren bidea (erlatiboa edo absolutua)
    # Bidea erlatiboa bada, script-aren kokapenetik ebazten da
    xml_path = Path(sys.argv[1]).resolve()
    urtea = sys.argv[2]

    if not urtea.isdigit():
        print("Urtea zenbaki bat izan behar da")
        sys.exit(1)

    if not xml_path.exists():
        logger.error("XML fitxategia ez da existitzen: %s", xml_path)
        sys.exit(1)

    logger.info("XML fitxategitik txostenak sortzen")
    logger.info("XML: %s", xml_path)

    # XML fitxategia XSLT bidez HTML txostenetan bihurtu
    # HTML Txostena: hilabeteka
    apply_xslt_html(
        xml_path,
        BASE_DIR / "scripts" / "stats_erreserbak_hilabeteka.xsl",
        DATA_DIR / f"txostena_erreserbak_hilabeteka_{urtea}.html"
    )

    # HTML Txostena: kabinaka
    apply_xslt_html(
        xml_path,
        BASE_DIR / "scripts" / "stats_erreserbak_kabinaka.xsl",
        DATA_DIR / f"txostena_erreserbak_kabinaka_{urtea}.html"
    )

    logger.info("Txostenak behar bezala sortu dira")

if __name__ == "__main__":
    ensure_dirs()
    main()
