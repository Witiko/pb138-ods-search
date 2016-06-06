---
documentclass: scrartcl
lang: sk
fontfamily: tgpagella
fontenc: T1
title: Vyhledávání dat v souboru ve formátu Open Document Spreadsheet (Java) Tým B
subtitle: Záverečná správa
author: Matej Majer
---

Pri práci na projekte som sa podieľal na tvorbe jadra v podobe Javovej implementácie v balíku `common`  a pripravil som JUnit testy pre výsledný projekt. 
V rámci implementácie jadra som pripravil základy metód na rozlišovanie vstupného Open Document Spreadsheet súboru podľa špecifikácie, transformáciu na jednoduchý XML súbor pomocou XSLT schémy, ktorú pripravila Svetlana, a validáciu tohto súboru. Ďalej som navrhol metódy na samotné vyhľadávanie samotného výrazu a konkrétnej bunky pomocou XPath technológií.
Pri tvorbe JUnit testov som sa snažil vytvoriť také testy, ktoré by pokryli všetky možné vstupy a výstupy projektu aby som odhalil prípadné chyby a nedostatky projektu. Pri vstupoch som sa zameriaval na závislosť na veľkosti písmen(case sensitivity) a celkovú alebo čiastočnú zhodu vstupného reťazca (exact / partial match). Pri výstupoch som sa zameriaval na to, aby program vrátil všetky možné výstupy (napr. z rôznych tabuliek).

