<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- Шаблон для корневого элемента Concerts -->
    <xsl:template match="/Concerts">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-width="21cm" page-height="29.7cm" margin="2cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">

                    <!-- Заголовок для расписания туров -->
                    <fo:block font-size="18pt" font-weight="bold" text-align="center" space-after="15pt">
                        Concerts
                    </fo:block>

                    <!-- Перебор каждого концерта -->
                    <xsl:for-each select="Concert">
                        <!-- Блок для каждого концерта -->
                        <fo:block border="1pt solid black" padding="10pt" margin-bottom="10pt">

                            <!-- Дата тура -->
                            <fo:block font-size="12pt" font-weight="bold">
                                Date: <xsl:value-of select="Date"/>
                            </fo:block>

                            <!-- Группа -->
                            <fo:block font-size="12pt">
                                Music Group: <xsl:value-of select="MusicGroup"/>
                            </fo:block>

                            <!-- Город -->
                            <fo:block font-size="12pt">
                                City: <xsl:value-of select="City"/>
                            </fo:block>

                            <!-- Место проведения -->
                            <fo:block font-size="12pt">
                                Venue: <xsl:value-of select="Venue"/>
                            </fo:block>

                            <!-- Организатор -->
                            <fo:block font-size="12pt">
                                Organizer: <xsl:value-of select="OrgName"/>
                            </fo:block>

                            <!-- Контактный номер организатора -->
                            <fo:block font-size="12pt">
                                Organizer Phone: <xsl:value-of select="OrgPhone"/>
                            </fo:block>
                        </fo:block>
                    </xsl:for-each>

                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>
