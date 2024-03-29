# CodinGameSpringChallenge2022

## About this repository

To whoever finds to this page. This code was written in the context of https://www.codingame.com/contests/spring-challenge-2022 a coding challenge over a total of 10 days of which I spent about 5 eventings on the challenge. Each participant had to write a bot for a simple RTS-style game.
Due to time pressure, essentially no tests were implemented, and there's still a bunch of commented out code in there. Also we couldn't use 3rd party libraries and therefore I implemented very crude versions of what well established libraries offer.

## Postmortem

With a ranking of 2543rd out of 7705 this was a proper, but surely not spectacular approach.
While the general architecture of my approach was very flexible, I was fighting with buggy behavior which slowed down my progression. Also debugging on the contest platform wasn't too easy because the tooling didn't allow it online (except from SysErr outputs)

This was mostly due to me not being super fit with this kind of programming (bot programming), so I couldn't use my anticipated dynamic strategy changes as opposed to just strategy adoptions.
In hindsight I also should've decided on the actions to take from a target centric point of view, rather than hero centric. But as I realized this, it was already too late to do such a big refactoring.

For a very first challenge of this kind I think it was good, but very much improvable.

**On to the next challenge :D**
