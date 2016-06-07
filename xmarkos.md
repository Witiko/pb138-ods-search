---
documentclass: scrartcl
lang: sk
fontfamily: tgpagella
fontenc: T1
title: Vyhledávání dat v souboru ve formátu Open Document Spreadsheet (Java) Tým B
subtitle: Záverečná správa
author: Svetlana Markosová
---

V rámci projektu som vytvorila XSLT transformáciu pôvodného dokumentu a k nemu som napísala XML schému. Navrhla som XPath výrazy použité pre vyhľadávanie podľa daných parametrov. V rámci balíka `gui` som spolupracovala na slovenskej lokalizácii.

Najskôr som preštudovala súbor `content.xml` obsahujúci dáta určené na prehľadávanie. Zistila som, že obsahuje nadbytok informácií nepotrebných pre fulltextové vyhľadávanie, čo okrem iného zvyšuje pravdepodobnosť vzniku chýb. Navyše reprezentácia dier v tabuľke je riešená zvláštnym spôsobom, a to jediným elementom nesúcim informáciu o počte za sebou nasledujúcich prázdnych riadkov alebo stĺpcov. Kvôli tomu by bolo počítanie súradníc bunky problematické. Vytvorila som XSLT transformáciu, ktorá daný dokument prevedie do jednoduchšieho XML formátu. Tento pomocou atribútov popisuje bunku súradnicami. Ďalej som pre takto vzniknutý súbor napísala XML schému.

V rámci balíka `common` som ešte pripravila XPath výrazy pre vyhľadávanie podľa určených parametrov. Jediný menší problém mohol predstavovať fakt, že XPath verzie 1 neponúka funkciu, ktorá vykoná hľadanie necitlivé na veľkosť písmen (XPath 2.0 ponúka funkciu `match()` s príznakom `-i`). Vyriešila som to prevádzaním veľkosti písmen celého výrazu pomocou XPath funkcie `translate()`. Tá si v implementácii pomáha javovými metódami `toLowerCase()` a `toUpperCase()`, ale na tej časti už pracovali kolegovia.