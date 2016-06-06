.PHONY: all clean
REPORTS=xnovot32 xmarkos
OUTPUT=$(addsuffix .pdf, $(REPORTS)) $(addsuffix .html, $(REPORTS)) \
			 $(addsuffix .dbk, $(REPORTS))
all: $(OUTPUT)

%.pdf: %.md
	pandoc -f markdown $< -t latex -o $@ --filter ./vlna.js

%.html: %.md
	pandoc -f markdown $< -t html -s -o $@ --filter ./vlna.js

%.dbk: %.md
	pandoc -f markdown $< -t docbook -s -o $@

clean:
	rm -f $(OUTPUT)
