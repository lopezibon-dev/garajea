import logging
import mysql.connector
from mysql.connector import Error
import csv
import sys
from pathlib import Path
from garajea_paths import DATA_DIR, DATUBASEA_DIR

# Logging sinplea
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
    """datu-base konexioa lortzen du"""
    return mysql.connector.connect(
        host="localhost",
        user="garajea",
        password="1234",
        database="garajea"
    )

def lortu_taula_zutabeak(conn, taula_izena: str):
    """
    Taularen zutabe guztiak lortzen ditu
    """
    cursor = conn.cursor()
    sql = """
        SELECT COLUMN_NAME
        FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = 'garajea' 
        AND TABLE_NAME = %s
        ORDER BY ORDINAL_POSITION
    """
    cursor.execute(sql, (taula_izena,))
    
    zutabeak = [row[0] for row in cursor.fetchall()]
    
    # Lista hutsa bada, None itzuli
    return zutabeak if zutabeak else None

def inportatu_csv(taula_izena: str, sarrera_fitxategia: Path):
    """
    CSV fitxategi batetik datuak taulara inportatzen ditu.
    
    GARRANTZITSUA: 
    - CSV-ak goiburua izan behar du zutabe izenekin
    - CSV-ko zutabeak taulako zutabeekin bat etorri behar dute
    - Taula BETI hustuko da inportatu aurretik (TRUNCATE)
    
    Args:
        taula_izena: Inportatu beharreko taularen izena
        sarrera_fitxategia: Sarrera CSV fitxategiaren izena
        
    Returns:
        0 ekintza arrakastaz betetzen bada; bestela, 0 baino handiagoko irteera-kodea
    """
    conn = None
    try:
        conn = lortu_konexioa()
        cursor = conn.cursor()
        
        # Taularen zutabeak eskuratu
        taula_zutabeak = lortu_taula_zutabeak(conn, taula_izena)

        if not taula_zutabeak:
            logger.error(f"'{taula_izena}' taula ez da existitzen edo ez du zutaberik")
            return 2        
        
        # CSV irakurri
        with open(sarrera_fitxategia, 'r', encoding='utf-8') as f:
            # sortu CSV fitxategia irakurriko duen iteratzailea: irakurlea
            irakurlea = csv.DictReader(f)
            csv_zutabeak = irakurlea.fieldnames

            # Egiaztatu CSV-ak goiburua duela
            if not csv_zutabeak:  # None edo hutsa da
                logger.error(f"CSV fitxategia hutsik dago edo ez du goibururik")
                return 3
            
            # EGIAZTATU: CSV-ko zutabe guztiak taulan daudela
            csv_set = set(csv_zutabeak)
            taula_set = set(taula_zutabeak)
            
            if not csv_set.issubset(taula_set):
                falta_direnak = csv_set - taula_set
                logger.error(f"CSV-ko zutabe hauek ez daude taulan: {falta_direnak}")
                return 4
            
            # EGIAZTATU: Taulako zutabe guztiak CSV-an daudela
            if not taula_set.issubset(csv_set):
                falta_direnak = taula_set - csv_set
                logger.error(f"Taulako zutabe hauek ez daude CSV-an: {falta_direnak}")
                return 4
            
            # Taula hustu (TRUNCATE bidez edozein autoincrement hasieratzen da)
            cursor.execute(f"TRUNCATE TABLE {taula_izena}")
            logger.info(f"'{taula_izena}' taula hustuta")
            
            # INSERT prestatu (CSV-ko zutabeen ordena erabiliz)
            placeholders_katea = ', '.join(['%s'] * len(csv_zutabeak))
            zutabeen_katea = ', '.join(csv_zutabeak)
            sql = f"INSERT INTO {taula_izena} ({zutabeen_katea}) VALUES ({placeholders_katea})"
            # Adib.: INSERT INTO KABINA (id, izena) VALUES (%s, %s) 

            kopurua = 0
            for errenkada in irakurlea:
                # Balioak atera CSV-ko zutabeen ordenan
                balioak = [errenkada[zutabe] if errenkada[zutabe] != '' else None 
                          for zutabe in csv_zutabeak]
                cursor.execute(sql, balioak)
                kopurua += 1
            
            conn.commit()
            logger.info(f"{kopurua} errenkada inportatuak '{taula_izena}' taulara")
            return 0
        
    except Error as e:
        logger.error(f"Errorea datu-basean datuak inportatzean: {e}")
        if conn:
            conn.rollback()
        return 5
        
    except FileNotFoundError:
        logger.error(f"Fitxategia ez da aurkitu: {sarrera_fitxategia}")
        return 6
        
    finally:
        if conn:
            conn.close()

def main():
    if len(sys.argv) < 3:
        print("Erabilera: python inportatu_taula.py <taula> <sarrera_fitxategia>")
        print("")
        print("OHARRA: Taula BETI hustuko da (TRUNCATE) inportatu aurretik!")
        print("OHARRA: CSV fitxategiak goiburua izan behar du zutabe izenekin!")
        sys.exit(1)
    
    taula_izena = sys.argv[1]
    sarrera_fitxategia = Path(sys.argv[2])
    
    try:
        irteera_kodea = inportatu_csv(taula_izena, DATUBASEA_DIR / sarrera_fitxategia)
        sys.exit(irteera_kodea)
    except Exception as e:
        logger.exception("Ustekabeko errorea inportazioan")
        sys.exit(99)            

if __name__ == "__main__":
    ensure_dirs()
    main()
