<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="text" encoding="UTF-8"/>
<!--
  XSLT honek XML erreserba_estatistikak
  CSV formatura bihurtzen ditu.
  Irteera testu lineala da (method="text").
-->  
<xsl:template match="/">
Hilabetea,Guztira<xsl:for-each select="erreserba_estatistikak/hilabete[1]/kabina">,<xsl:value-of select="@izena"/></xsl:for-each>
<xsl:text>&#10;</xsl:text>
<xsl:for-each select="erreserba_estatistikak/hilabete">
<xsl:call-template name="hilabete-izena">
  <xsl:with-param name="zenbakia" select="@zenbakia"/>
</xsl:call-template>,<xsl:value-of select="@kopurua"/><xsl:for-each select="kabina">,<xsl:value-of select="@kopurua"/></xsl:for-each>
<xsl:text>&#10;</xsl:text>
</xsl:for-each>
# Txostena: Urteko Erreserbak,<xsl:value-of select="erreserba_estatistikak/@urtea"/> (Hilabeteka)
Urteko guztizkoa,<xsl:value-of select="erreserba_estatistikak/@urtekoGuztizkoa"/>

</xsl:template>
<xsl:template name="hilabete-izena">
    <xsl:param name="zenbakia"/>

    <xsl:choose>
      <xsl:when test="$zenbakia = 1">Urtarrila</xsl:when>
      <xsl:when test="$zenbakia = 2">Otsaila</xsl:when>
      <xsl:when test="$zenbakia = 3">Martxoa</xsl:when>
      <xsl:when test="$zenbakia = 4">Apirila</xsl:when>
      <xsl:when test="$zenbakia = 5">Maiatza</xsl:when>
      <xsl:when test="$zenbakia = 6">Ekaina</xsl:when>
      <xsl:when test="$zenbakia = 7">Uztaila</xsl:when>
      <xsl:when test="$zenbakia = 8">Abuztua</xsl:when>
      <xsl:when test="$zenbakia = 9">Iraila</xsl:when>
      <xsl:when test="$zenbakia = 10">Urria</xsl:when>
      <xsl:when test="$zenbakia = 11">Azaroa</xsl:when>
      <xsl:otherwise>Abendua</xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>