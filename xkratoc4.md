---
documentclass: scrartcl
lang: cs
fontfamily: tgpagella
fontenc: T1
title: Vyhledávání dat v souboru ve formátu Open Document Spreadsheet (Java) Tým B
subtitle: Závěrečná zpráva
author: Petr Kratochvíla
---

Moje práce na projektu spočívala v implementaci rozhraní příkazové řádky v balíčku `cz.muni.fi.pb138.odsSearch.cli` pro konzolovou verzi programu. Aplikace zpracovává argumenty příkazové řádky určující způsob vyhledávání, seznam prohledávaných souborů a hledaný řetězec. Součástí je i nápověda se seznamem všech argumentů a řetězec způsobou použití.

Samotná implementace využívá knihovnu *Apache Commons CLI* ke zpracování argumentů a tvorbu nápovědy. Jedná se o knihovnu určenou spíše pro rozsáhlejší aplikace s větší množinou vstupních argumentů. Nicméně vybral jsem tuto variantu s přihlédnutím k možnosti dalšího rozšíření a výukový chrakter aplikace.   

Po zpracování argumentů je postupně každý soubor společně s argumenty zpracován jdárem aplikace a výsledky předány na standardní výstup. Aplikace používá GNU-like syntax argumentů.



