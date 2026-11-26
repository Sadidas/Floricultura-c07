import mysql.connector
from mysql.connector import Error


class PlantaDAO:
    def __init__(self, host="localhost", user="root", password="", database="floricultura"):
        self.config = {
            "host": host,
            "user": user,
            "password": password,
            "database": database,
        }

    def inserir_planta(self, nome, especie, preco, estoque):
        conexao = None
        cursor = None
        try:
            conexao = mysql.connector.connect(**self.config)
            cursor = conexao.cursor()
            sql = """
                INSERT INTO planta (nome, especie, preco, estoque)
                VALUES (%s, %s, %s, %s)
            """
            cursor.execute(sql, (nome, especie, preco, estoque))
            conexao.commit()
            return cursor.lastrowid
        except Error:
            if conexao and conexao.is_connected():
                conexao.rollback()
            raise
        finally:
            if cursor:
                cursor.close()
            if conexao and conexao.is_connected():
                conexao.close()

    def listar_plantas(self):
        conexao = None
        cursor = None
        try:
            conexao = mysql.connector.connect(**self.config)
            cursor = conexao.cursor(dictionary=True)
            cursor.execute("SELECT id, nome, especie, preco, estoque FROM planta ORDER BY id")
            return cursor.fetchall()
        finally:
            if cursor:
                cursor.close()
            if conexao and conexao.is_connected():
                conexao.close()

    def buscar_por_especie(self, especie):
        conexao = None
        cursor = None
        try:
            conexao = mysql.connector.connect(**self.config)
            cursor = conexao.cursor(dictionary=True)
            sql = """
                SELECT id, nome, especie, preco, estoque
                FROM planta
                WHERE especie = %s
                ORDER BY id
            """
            cursor.execute(sql, (especie,))
            return cursor.fetchall()
        finally:
            if cursor:
                cursor.close()
            if conexao and conexao.is_connected():
                conexao.close()

    def atualizar_preco(self, id_planta, novo_preco):
        conexao = None
        cursor = None
        try:
            conexao = mysql.connector.connect(**self.config)
            cursor = conexao.cursor()
            sql = "UPDATE planta SET preco = %s WHERE id = %s"
            cursor.execute(sql, (novo_preco, id_planta))
            conexao.commit()
            return cursor.rowcount > 0
        except Error:
            if conexao and conexao.is_connected():
                conexao.rollback()
            raise
        finally:
            if cursor:
                cursor.close()
            if conexao and conexao.is_connected():
                conexao.close()

    def remover_planta(self, id_planta):
        conexao = None
        cursor = None
        try:
            conexao = mysql.connector.connect(**self.config)
            cursor = conexao.cursor()
            cursor.execute("DELETE FROM planta WHERE id = %s", (id_planta,))
            conexao.commit()
            return cursor.rowcount > 0
        except Error:
            if conexao and conexao.is_connected():
                conexao.rollback()
            raise
        finally:
            if cursor:
                cursor.close()
            if conexao and conexao.is_connected():
                conexao.close()

    def valor_total_estoque(self):
        conexao = None
        cursor = None
        try:
            conexao = mysql.connector.connect(**self.config)
            cursor = conexao.cursor()
            cursor.execute("SELECT COALESCE(SUM(preco * estoque), 0) FROM planta")
            resultado = cursor.fetchone()
            return float(resultado[0])
        finally:
            if cursor:
                cursor.close()
            if conexao and conexao.is_connected():
                conexao.close()
