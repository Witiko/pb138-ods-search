<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : transform.xsl
    Created on : 26-05-2016
    Author     : Svetlana Markosová <s.markosova@mail.muni.cz>
                 Vít Novotný <witiko@mail.muni.cz>
    Description: Transforms the contents of an .ods file into an XML file.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
                xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
                xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" version="1.0">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:template match="text()|@*"/>
    <xsl:template match="office:spreadsheet">
        <document xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:noNamespaceSchemaLocation="transformed.xsd">
            <xsl:for-each select="table:table">
                <table name="{@table:name}">
                    <xsl:for-each select="table:table-row">
                        <xsl:for-each select="table:table-cell[.//text:p]">
                            <cell rown="{sum((..|../preceding-sibling::table:table-row)/@table:number-rows-repeated) +
                                         count((..|../preceding-sibling::table:table-row)[not(@table:number-rows-repeated)])}"
                                  coln="{sum((.|preceding-sibling::table:table-cell)/@table:number-columns-repeated) +
                                         count((.|preceding-sibling::table:table-cell)[not(@table:number-columns-repeated)])}">
                                <xsl:value-of select="text:p"/>
                            </cell>
                        </xsl:for-each>
                    </xsl:for-each>
                </table>
            </xsl:for-each>
        </document>
    </xsl:template>
</xsl:stylesheet>