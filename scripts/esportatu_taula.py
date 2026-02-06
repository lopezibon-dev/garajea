import logging
import mysql.connector
from mysql.connector import Error
import csv
import sys
from pathlib import Path
from garajea_paths import DATA_DIR, DATUBASEA_DIR

# Logging
LOG_FILE = r"C:\logs\garajea_python.log"

# Handler-ak banaka konfiguratu
file_handler = logging.FileHandler(LOG_FILE, encoding="utf-8")
file_handler.setFormatter(logging.Formatter(fmt='%(asctime)s [PYTHON] %(levelname)s: %(message)s', datefmt='%Y-%m-%d %H:%M:%S'))

console_handler = logging.StreamHandler(sys.stdout)
console_handler.setFormatter(logging.Formatter('%(levelname)s: %(message)s'))  # Sinpleagoa

logging.basicConfig(
    level=logging.INFO,
    handlers=[file_handler, console_handler]
)

logger = logging.getLogger(__name__)

def ensure_dirs():
    # data direkorioa sortu, behar izanez gero
    DATA_DIR.mkdir(exist_ok=True)
    # data/datubasea direkorioa sortu, behar izanez gero
    DATUBASEA_DIR.mkdir(exist_ok=True)


def lortu_konexioa():
    """BBDD konexioa lortzen du"""
    return mysql.connector.connect(
        host="localhost",
        user="garajea",
        password="1234",
        database="garajea"
    )

def esportatu_csv(taula_izena: str, irteera_fitxategia: Path):
    """
    Taula osoa CSV-ra esportatzen du.
    
    Args:
        taula_izena: Esportatu beharreko taularen izena
        irteera_fitxategia: Irteerako CSV fitxategiaren izena
        
    Returns:
        True arrakasta bada, bestela False
    """
    conn = None
    try:
        conn = lortu_konexioa()
        cursor = conn.cursor()
        
        # Taulako datu guztiak eskuratu
        sql = f"SELECT * FROM {taula_izena}"
        cursor.execute(sql)
        
        errenkadak = cursor.fetchall()
        zutabe_izenak = [desc[0] for desc in cursor.description] # type: ignore
        
        # CSV-ra idatzi
        with open(irteera_fitxategia, 'w', newline='', encoding='utf-8') as f:
            # sortu CSV fitxategian idatziko duen iteratzailea: idazlea
            idazlea = csv.writer(f)
            idazlea.writerow(zutabe_izenak)  # Goiburua
            idazlea.writerows(errenkadak)
        
        logger.info(f"{len(errenkadak)} errenkada esportatuak '{taula_izena}' taulatik '{irteera_fitxategia}' fitxategira")
        return True
        
    except Error as e:
        logger.error(f"Errorea datu-baseko kontsultan: {e}")
        return False
      
    except FileNotFoundError:
        logger.error(f"Fitxategia ezin da sortu: {irteera_fitxategia}")
        return False
      
    finally:
        if conn:
            conn.close()

def main():
    if len(sys.argv) < 3:
        print("Erabilera: python esportatu_taula.py <taula> <irteera_fitxategia>")
        sys.exit(1)

    taula_izena = sys.argv[1]
    irteera_fitxategia = Path(sys.argv[2])

    try:
        ondo = esportatu_csv(taula_izena, DATUBASEA_DIR / irteera_fitxategia)
        if ondo:
            sys.exit(0)
        else:
            sys.exit(2)
    except Exception as e:
        logger.exception("Ustekabeko errorea esportazioan")
        sys.exit(3)

if __name__ == "__main__":
    ensure_dirs()
    main()
