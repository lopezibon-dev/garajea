import logging
from pathlib import Path
import sys

from db_lortu_datuak import fetch_erreserba_stats
from sortu_estatistikak import build_stats_dict
from sortu_xml import write_stats_xml
from transform_xslt import apply_xslt

# Logging
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
    DATA_DIR.mkdir(exist_ok=True)

def main():

    if len(sys.argv) != 2:
        print("Erabilera:")
        print("sortu_txostenak.py <urtea>")
        sys.exit(1)

    urtea = int(sys.argv[1])

    logger.info("%s urterako estatistikak sortzen", urtea)

    rows = fetch_erreserba_stats(urtea)
    stats = build_stats_dict(urtea, rows)

    xml_file  = DATA_DIR / f"stats_erreserbak_{urtea}.xml"
    write_stats_xml(stats, xml_file )

    logger.info("XML sortua: %s", xml_file)

    # Txostena : hilabeteka
    apply_xslt(
        xml_file,
        BASE_DIR / "scripts" / "stats_erreserbak_hilabeteka.xsl",
        DATA_DIR / f"txostena_erreserbak_hilabeteka_{urtea}.html"
    )

    # Txostena : kabinaka
    apply_xslt(
        xml_file,
        BASE_DIR / "scripts" / "stats_erreserbak_kabinaka.xsl",
        DATA_DIR / f"txostena_erreserbak_kabinaka_{urtea}.html"
    )
if __name__ == "__main__":
    ensure_dirs()
    main()
