<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <!-- Определение шаблона для основного документа -->
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">

        <!-- Основная структура HTML -->
        <html>
            <head>
                <title>Concerts</title>
                <style>
                    table {
                    width: 100%;
                    border-collapse: collapse;
                    }
                    th, td {
                    padding: 8px;
                    text-align: left;
                    border: 1px solid #ddd;
                    }
                    th {
                    background-color: #f2f2f2;
                    }
                </style>
            </head>
            <body>
                <h1>Concert</h1>

                <!-- Таблица для отображения расписания -->
                <table>
                    <tr>
                        <th>Date</th>
                        <th>Music Group</th>
                        <th>City</th>
                        <th>Venue</th>
                        <th>Organizer</th>
                        <th>Organizer Phone</th>
                    </tr>

                    <!-- Процессинг каждого элемента Concert -->
                    <xsl:for-each select="Concerts/Concert">
                        <tr>
                            <td><xsl:value-of select="Date"/></td>
                            <td><xsl:value-of select="MusicGroup"/></td>
                            <td><xsl:value-of select="City"/></td>
                            <td><xsl:value-of select="Venue"/></td>
                            <td><xsl:value-of select="OrgName"/></td>
                            <td><xsl:value-of select="OrgPhone"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>

    </xsl:template>
</xsl:stylesheet>
