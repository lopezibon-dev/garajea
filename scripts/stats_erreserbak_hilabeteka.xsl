<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output method="html" encoding="UTF-8" indent="yes"/>
  
  <xsl:template match="/">
    <html>
      <head>
        <title>
          Txostena: Erreserbak – <xsl:value-of select="erreserba_estatistikak/@urtea"/> (Hilabeteka)
        </title>
        <meta charset="UTF-8"/>
        <link rel="stylesheet" href="txostena.css"/>
      </head>
      <body>

        <header>
          <h1>
            Txostena: Erreserbak – <xsl:value-of select="erreserba_estatistikak/@urtea"/> (Hilabeteka)
          </h1>
          <p>
            Urteko guztizkoa:
            <strong><xsl:value-of select="erreserba_estatistikak/@urtekoGuztizkoa"/></strong>
          </p>
        </header>

        <!-- mobile Bista-->
        <section class="mobile-bista">
          <xsl:apply-templates select="erreserba_estatistikak/hilabete" mode="mobile"/>
        </section>

        <!-- desktop Bista -->
        <section class="desktop-bista">
          <xsl:call-template name="taula-desktop"/>
        </section>

        <footer>
          <p>DIY Garajea</p>
        </footer>

      </body>
    </html>
  </xsl:template>
  
  <xsl:template match="hilabete" mode="mobile">
    <section class="hilabete">
      <h2>
        <xsl:call-template name="hilabete-izena">
          <xsl:with-param name="zenbakia" select="@zenbakia"/>
        </xsl:call-template>
        –
        Guztira:
        <xsl:value-of select="@kopurua"/>
      </h2>

      <ul>
        <xsl:apply-templates select="kabina"/>
      </ul>
    </section>
  </xsl:template>

  <xsl:template match="kabina">
    <li>
      <xsl:value-of select="@izena"/>:
      <xsl:value-of select="@kopurua"/>
    </li>
  </xsl:template>
  
  <xsl:template name="taula-desktop">
    <table>
      <thead>
        <tr>
          <th>Hilabetea</th>
          <th>Guztira</th>
          <xsl:for-each select="erreserba_estatistikak/hilabete[1]/kabina">
            <th><xsl:value-of select="@izena"/></th>
          </xsl:for-each>
        </tr>
      </thead>

      <tbody>
        <xsl:for-each select="erreserba_estatistikak/hilabete">
          <tr>
            <td>
              <xsl:call-template name="hilabete-izena">
                <xsl:with-param name="zenbakia" select="@zenbakia"/>
              </xsl:call-template>
            </td>
            <td><xsl:value-of select="@kopurua"/></td>

            <xsl:for-each select="kabina">
              <td><xsl:value-of select="@kopurua"/></td>
            </xsl:for-each>
          </tr>
        </xsl:for-each>
      </tbody>
    </table>
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

