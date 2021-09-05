Program: MATERIAL
Team: SCRIPTS
Columbia University

============================================
Scripts-Morph Version 11.5
Supported Languages: English, Swahili, Tagalog, Somali, Lithuanian, Bulgarian, Pashto, Farsi, Kazakh and Georgian
August 26th, 2021
============================================

============================================
Updates since Version 10.0
============================================
- Adding the support for Georgian (KA)
- Preserving original casing in segmentation


============================================
I/O
============================================
Input: 
- Language Code (ISO 639-2) (EN=English, SW=Swahili, TL=Tagalog, SO=Somali, LT=Lithuanian BG=Bulgarian, PS=Pashto, FA=Farsi, KK=Kazakh or KA=Georgian)
- Plain Text (standard I/O, file I/O and directory I/O)
An analyzed word in the output contains the following key/value pairs:
- word : The original word
- stem: Word stem
- prefixes: “+”-separated word prefixes
- suffixes: “+”-separated word suffixes
- pos : {ADJ, ADP, ADV, AUX, CCONJ, DET, INTJ, PART, NOUN, NUM, PRON, PROPN, PUNCT, SCONJ, SYM, VERB, X}
- pos_props: A list of the top probable POS tags per word and their probabilities
- tense: {PAST, PRES, FUT, NA}
- number: {SG, PL, NA}
- index: An integer representing the index of the word in the sentence.

Notes:
- POS values correspond to {Adjective, Adposition, Adverb, Auxiliary, Conjunction, Determiner, Interjection, Particle, Noun, Number, Pronoun, Proper Noun, Punctuation, Subjunctive Conjunction, Symbol, Verb, Other}
- Tense values correspond to {Past, Present, Future and Not-Applicable}
- Number values correspond to {Singular, Plural and Not-Applicable}
- Tense is only applicable for AUX and VERB tags
- Number is only applicable for NOUN and PROPN tags.
- PROPN is replaced by NOUN in Farsi.

Example

Input:
Language Code: FA
Text: پس قوم درروز هفتمین آرام گرفتند.

Output:
[[{"word":"پس","pos":"ADV","pos_props":{"ADV":0.98896,"ADP":0.01102,"NOUN":0.00002},"tense":"NA","num":"NA","index":0,"number":"NA","prefixes":"","suffixes":"","stem":"پس"},{"word":"قوم","pos":"NOUN","pos_props":{"NOUN":1.00000},"tense":"NA","num":"PL","index":1,"number":"PL","prefixes":"","suffixes":"","stem":"قوم"},{"word":"درروز","pos":"NOUN","pos_props":{"NOUN":0.81636,"ADV":0.18094,"ADP":0.00254,"ADJ":0.00016},"tense":"NA","num":"SG","index":2,"number":"SG","prefixes":"در","suffixes":"","stem":"روز"},{"word":"هفتمین","pos":"ADJ","pos_props":{"ADJ":1.00000},"tense":"NA","num":"NA","index":3,"number":"NA","prefixes":"","suffixes":"ین","stem":"هفتم"},{"word":"آرام","pos":"ADJ","pos_props":{"ADJ":0.67112,"NOUN":0.30396,"ADV":0.02492},"tense":"NA","num":"NA","index":4,"number":"NA","prefixes":"","suffixes":"","stem":"آرام"},{"word":"گرفتند","pos":"VERB","pos_props":{"VERB":1.00000},"tense":"PAST","num":"NA","index":5,"number":"NA","prefixes":"","suffixes":"ند","stem":"گرفت"},{"word":".","pos":"PUNCT","pos_props":{"PUNCT":1.00000},"tense":"NA","num":"NA","index":6,"number":"NA","prefixes":"","suffixes":"","stem":"."}]]

============================================
Usage:
============================================

Using the JAR file:
--------------------------------------------
Requirements: JRE 1.8+
Execution:
Standalone - Standard I/O:
Usage: java -jar scripts-morph-v11.5.jar <language> <text>
Standalone - File I/O:
Usage: java -jar scripts-morph-v11.5.jar <language> <input-file> <output-json-file>
Standalone - Directory I/O:
Usage: java -jar scripts-morph-v11.5.jar <language> <input-directory> <output-json-directory>
Socket Server Initialization:
Usage: java -jar scripts-morph-v11.5.jar <port> <language>
Socket Client - Standard I/O:
Usage: java -jar scripts-morph-v11.5.jar <port> <language> <line>
Socket Client - File I/O:
Usage: java -jar scripts-morph-v11.5.jar <port> <language> <input-file> <output-json-file>
Socket Client - Directory I/O:
Usage: java -jar scripts-morph-v11.5.jar <port> <language> <input-directory> <output-json-directory>

Notes:
- Make sure to keep the "resources" directory in the same location as that of the JAR file.
- Every line in the input corresponds to a list of Json objects in the output.

Using the Docker image:
--------------------------------------------
Running the analyzer requires Docker installation. No other installation is required.
Execution:
1- Load the Docker image (only once):
docker load -i scripts-morph-v11.5.tar
2- Run the analyzer in a similar fashion to how it is run from the Jar, but with directory mappings if necessary. For example:
docker run  -v=<input-directory>:/root/in -v=<output-directory>:/root/out material/scripts-morph:11.5 TGL /root/in /root/out
Where the input directory is mounted to /root/in , and the output directory is mounted to /root/out. /root/in and /root/out are then used as parameters to the JAR within the image.

============================================
Ramy Eskander
rnd2110@columbia.edu

August 26th, 2021

