---
documentclass: scrartcl
lang: sk
fontfamily: tgpagella
fontenc: T1
title: Vyhledávání dat v souboru ve formátu Open Document Spreadsheet (Java) Tým B
subtitle: Záverečná správa
author: Svetlana Markosová
---

V rámci projektu som najskôr preštudovala súbor `content.xml` obsahujúci dáta určené na prehľadávanie. Kvôli nevhodnej reprezentácii polohy buniek som pripravila XSLT transformáciu tohto dokumentu do jednoduchšieho XML formátu, v ktorom sa budú dať ľahko určiť súradnice jednotlivých buniek. Ďalej som pre takto vzniknutý súbor napísala XML schému. V rámci balíka `common` som pripravila XPath výrazy pre vyhľadávanie podľa určených parametrov. V rámci balíka `gui` som spolupracovala na slovenskej lokalizácii.
