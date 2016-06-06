.PHONY: all clean implode
SLIDES=slides.pdf
REPORTS=xnovot32 xmarkos
OUTPUT=$(addsuffix .pdf, $(REPORTS)) $(addsuffix .html, $(REPORTS)) \
			 $(addsuffix .dbk, $(REPORTS)) $(SLIDES)

all: $(OUTPUT) clean

%.pdf: %.md
	pandoc -f markdown $< -t latex -o $@ --filter ./vlna.js

%.html: %.md
	pandoc -f markdown $< -t html -s -o $@ --filter ./vlna.js

%.dbk: %.md
	pandoc -f markdown $< -t docbook -s -o $@

$(SLIDES): $(subst .pdf,.tex,$(SLIDES))
	latexmk -pdf slides

clean:
	latexmk -c
	rm -f slides.nav slides.snm slides.vrb slides.markdown.in \
		slides.markdown.out slides.markdown.lua

implode:
	rm -f $(OUTPUT)
