# LSA-Bot
[![License: GPLv3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Copyright : (C) 2004 Salvatore La Bua [slabua(at)gmail.com](mailto:slabua@gmail.com)  
http://www.slblabs.com/projects/lsabot  
http://github.com/slabua/lsabot  

---

## IMPORTANT NOTICE
1. The software is by no means complete and is still at a very early stage.
2. I am in the process of cleaning the code a bit, removing the unnecessary
   files and finding a solution to host the large raw data files needed by the
   software.
3. The preliminary scripts needed to preprocess the text are included in the
   repository but it is not given yet a clear set of instructions to run them.
4. the *data* folder, due to its large dimension, has been compressed and needs
   to be extracted in order to work with LSA-Bot.  
   The archive is also hosted at:
   https://bitbucket.org/slabua/lsabot/downloads/data.tar.bz2

---

## Introduction to the Project

- LSA-Bot is a new, powerful kind of Chat-bot incentred on Latent Semantic
  Analysis.
- Using LSA it is possible to make relationships among words and vectors,
  permitting to realize an intelligent chat-bot that can understand human
  language and answer as well.

## Some information about LSA-Bot

- I developed LSA-bot at university since 12-sept-2004 (first class birthdate).
- LSA-Bot is written in Java and it works thanks to the LSA (Latent Semantic
  Analysis) theory applied to a large amount of text documents (corpus). There
  are many Chat-bot systems, most of them are using the AIML language to
  recognize users’ questions and bots can answer to the users, but the
  botmaster has to think about all kind of question a user can make to the bot.
- Using LSA is possible to give something intelligence to the chat-bot,
  permitting to ignore, for instance, wrong words, stop-words and all isn’t
  needed in the meaning of a sentence.
- LSA-bot uses the vectors related to every words found in the corpus to
  compute the ‘distance’ between user’s question and all possible answers, that
  can be simpliest sentences, small documents, or whatever the programmer wish
  to do. Word’s vectors are obtaines using the Singular Value Decomposition
  (SVD) onto the matrix built from words’ occurrences in the documents, using
  Matlab or other software that permit a singular value decomposition. Obtained
  the vectors we need, LSA-bot uses them to create vectors for every words, and
  every question a user can make. The distance among the question and
  verosimilar answers can be done by compute the cosine distance, rejection
  over projection, tanimoto… The answer related to the vector that satisfy the
  minimum distance will be shown to the user.
- Another feature is that the knowledge-base of LSA-bot can be improved
  (learn-mode) by specify a new sentence the bot has to learn; a new
  representing vector will be computed and added to others.

## Resources

1. [ResearchGate Thesis publication](https://goo.gl/LA3P5R)

