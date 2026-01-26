import logging
import mysql.connector
from mysql.connector import Error

logger = logging.getLogger(__name__)

def get_connection():
    return mysql.connector.connect(
        host="localhost",
        user="garajea",
        password="1234",
        database="garajea"
    )

def fetch_erreserba_stats(urtea: int):
    """
    Devuelve filas agregadas:
    (hilabetea, kabina_izena, kopurua)
    """
    sql = """
        SELECT
            MONTH(e.hasiera) AS hilabetea,
            k.izena          AS kabina_izena,
            COUNT(*)         AS kopurua
        FROM ERRESERBA e
        JOIN KABINA k ON e.kabina_id = k.kabina_id
        WHERE YEAR(e.hasiera) = %s
        GROUP BY MONTH(e.hasiera), k.izena
        ORDER BY MONTH(e.hasiera), k.izena
    """

    conn = None
    try:
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute(sql, (urtea,))
        rows = cursor.fetchall()
        return rows

    except Error as e:
        logger.error("Errorea BBDD kontsultan: %s", e)
        raise

    finally:
        if conn:
            conn.close()
