<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>

    <xsl:template match="/">
        <xsl:text>id;text</xsl:text>
        <xsl:text>&#xa;</xsl:text>
        <xsl:apply-templates select="*"/>
    </xsl:template>

    <xsl:template match="//Strings">
        <xsl:value-of select="id"/>,<xsl:value-of select="text"/>
    </xsl:template>

</xsl:stylesheet>