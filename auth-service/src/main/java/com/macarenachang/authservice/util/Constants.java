package com.macarenachang.authservice.util;

public class Constants {
		public static final String CLAVE="1234"; 
		public static final long TIEMPO_VIDA = 86_400_000; // 1 day. Tiempo de vida de nuestro token  en milisegundos. 1 dia
		public static final String ENCABEZADO = "Authorization"; //nombre del encabezado
		public static final String PREFIJO_TOKEN = "Bearer ";//prefijo que va a venir delante del token
		
		public static String getClave() {
			return CLAVE;
		}
		public static long getTiempoVida() {
			return TIEMPO_VIDA;
		}
		public static String getEncabezado() {
			return ENCABEZADO;
		}
		public static String getPrefijoToken() {
			return PREFIJO_TOKEN;
		}



}
