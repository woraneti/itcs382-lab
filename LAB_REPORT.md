# Lab Report, Code Quality and Design Quality

Repository URL:

## Tools

| Item | Version or name |
| --- | --- |
| JDK | |
| Maven | |
| IDE | |
| SonarQube for IDE | |
| AI tool and exact model | |

## Task 1. SonarQube on the starter code

Commit of the starter, before any change: 013de8f

Table 1. SonarQube issues in the starter code

| No. | Rule | File:line | Message |
| --- | --- | --- | --- |
| 1 | java:S106 | Main.java:9 | Replace this use of System.out by a logger. |
| 2 | java:S106 | Main.java:15 | Replace this use of System.out by a logger. |
| 3 | java:S106 | Main.java:17 | Replace this use of System.out by a logger. |
| 4 | java:S106 | Main.java:22 | Replace this use of System.out by a logger. |
| 5 | java:S2143 | ParcelService.java:1 | Use the "java.time" API for date and time. |
| 6 | java:S1128 | ParcelService.java:6 | Remove this unused import 'java.util.Date'. |
| 7 | java:S1068 | ParcelService.java:12 | Remove this unused "maxParcels" private field. |
| 8 | java:S106 | ParcelService.java:30 | Replace this use of System.out by a logger. |
| 9 | java:S4973 | ParcelService.java:42 | Strings and Boxed types should be compared using "equals()". |
| 10 | java:S106 | ParcelService.java:67 | Replace this use of System.out by a logger. |
| 11 | java:S106 | ParcelService.java:69 | Replace this use of System.out by a logger. |
| 12 | java:S2095 | ParcelService.java:76 | Use try-with-resources or close this "FileWriter" in a "finally" clause. |
| 13 | java:S108 | ParcelService.java:80 | Remove this block of code, fill it in, or add a comment explaining why it is empty. |

## Task 2. AI review of the starter code

| Item | Value |
| --- | --- |
| Date | 21 October 2026 |
| AI tool and exact model | ChatGPT, GPT-5 |
| Commit reviewed | 013de8f |
| How the AI read the files (attached the files, opened the folder, or pasted the files) | attached the files |
| Output file in the reviews folder | reviews/parcelpoint-review.md |

Table 2. All findings on the starter code

| No. | File:line | Finding | Found by (SonarQube, AI or both) | SonarQube rule or course term | Correct? (yes or no, and why) |
| --- | --- | --- | --- | --- | --- |
| 1 | ParcelService.java:42 | `getCode() == code` compares String references, so pickup never finds the parcel | both | java:S4973, probable bug | yes, because the run printed `Fee: 0.0` and the parcel stayed STORED after `pickup P1 5` |
| 2 | ParcelService.java:76 | The FileWriter is never closed, so the buffered rows are never written | both | java:S2095, probable bug (resource leak) | yes, because parcels.csv was empty (0 bytes) after the run |
| 3 | ParcelService.java:80 | The empty catch block hides a failed save | both | java:S108, code smell (empty block) | yes, because `catch (IOException e) { }` has no statement |
| 4 | ParcelService.java:6 | Unused import `java.util.Date` | both | java:S1128, code smell (unused import) | yes, because nothing in the file uses Date |
| 5 | ParcelService.java:12 | Unused field `maxParcels` | both | java:S1068, code smell (unused field) | yes, because nothing reads maxParcels |
| 6 | Main.java:9, 15, 17, 22; ParcelService.java:30, 67, 69 | System.out is used instead of a logger | SonarQube | java:S106 | yes, but in Main the printing is the output of a command-line program |
| 7 | ParcelService.java:1 | Use the java.time API for date and time | SonarQube | java:S2143 | yes, because the only use of Date is the unused import on line 6 |
| 8 | Main.java:13, 17 | Arguments are read without checking their count, so `store` alone throws an exception | AI | probable bug | yes, because words[1] is read without checking words.length |
| 9 | Main.java:13, 17 | Invalid number text throws NumberFormatException and ends the program | AI | probable bug | yes, because nothing catches the exception from Integer.parseInt |
| 10 | Main.java:11 | split(" ") gives empty words for repeated spaces and ignores tabs | AI | probable bug | yes, because `store  P1` gives an empty words[1] |
| 11 | Parcel.java:9; Locker.java:8; ParcelService.java:26, 56-61 | Sizes are not checked to be 1 to 3, so size 0 is stored and charged the large fee | AI | probable bug | yes, because the constructors accept any int and fee() returns the 20-baht rate for any size other than 1 or 2 |
| 12 | ParcelService.java:52-54 | Negative days are accepted as free storage | AI | probable bug | yes, because `days <= 3` is also true for negative days |
| 13 | ParcelService.java:24-29 | Duplicate codes are allowed, and the same Parcel object can be stored twice | AI | probable bug | partly, because duplicate codes are possible, but Main.java:13 creates a new Parcel for every store command, so the program cannot store the same object twice |
| 14 | ParcelService.java:49, 53-54 | pickUp returns 0 both when no parcel matches and when the pickup is free | AI | probable bug | yes, because line 49 returns 0 when nothing matches, which is why the run printed `Fee: 0.0` instead of an error |
| 15 | ParcelService.java:78 | CSV fields are not escaped | AI | probable bug | yes, but minor, because a code that contains a comma would add a column |
| 16 | Main.java:6 | The parameter args is unused | AI | code smell (unused parameter) | no, because args is part of the required signature of main |
| 17 | ParcelService.java:53, 57, 59, 61 | The free-day limit 3 is repeated four times | AI | code smell (repeated literal) | yes, because changing the limit means editing four places |
| 18 | Main.java:6-25 | main chooses between unrelated commands in one if-else chain | AI | logical cohesion | yes, because one chain picks storing, pickup, listing, staff report or quit |
| 19 | ParcelService.java:64-71; Main.java:19, 21 | The boolean forStaff picks which report printParcels prints | AI | control coupling, logical cohesion | yes, because Main passes true or false to choose a branch inside printParcels |
| 20 | ParcelService.java:24-78 | ParcelService allocates lockers, changes status, prints the SMS, computes fees, formats reports and writes the file | AI | weak class cohesion | yes, because the class has several jobs and fee() uses no field of the class |
| 21 | Parcel.java:7; ParcelService.java:28, 44, 67-68, 78 | The field status is public and ParcelService changes it directly | AI | non-private field, content coupling | yes, because ParcelService assigns parcel.status on lines 28 and 44 |
| 22 | ParcelService.java:20-21; Main.java:22 | getLockers returns the internal list, and Main reaches through it to count free lockers | AI | exposed internal collection, reach-through coupling | yes, because line 21 returns the field itself and Main calls service.getLockers().stream() with Locker::isFree |
| 23 | ParcelService.java:52 | fee receives the whole Parcel but reads only its size | AI | stamp coupling | yes, because fee calls only parcel.getSize() |
| 24 | ParcelService.java:24, 33; Main.java:14-15 | storeParcel returns the whole Locker, but Main needs only its number | AI | stamp coupling | yes, because Main calls only locker.getNumber() |
| 25 | Locker.java:5; Parcel.java:6; ParcelService.java:26, 56-61; Main.java:13 | Locker, Parcel and ParcelService must all read the sizes 1, 2 and 3 the same way | AI | connascence of convention (meaning) | yes, because the meaning of each number is only a comment, and line 26 compares the numbers as capacity |
| 26 | Parcel.java:13; ParcelService.java:28, 44, 68 | Status values are strings that must be spelled the same in two classes | AI | connascence of convention (meaning) | yes, because "STORED" is written on line 28 and compared on line 68, and a typo still compiles |
| 27 | Parcel.java:9; Locker.java:8; Main.java:13; ParcelService.java:15-17 | Constructors take neighbouring arguments of the same type, so swapped arguments compile | AI | connascence of position | yes, because swapping code and studentId in new Parcel(...) still compiles |

2.1 Name two problems that only the AI review found.

First, control coupling in ParcelService.java:64 and Main.java:19 and 21, where the boolean forStaff chooses which report printParcels prints (row 19). Second, connascence of convention for the parcel size, where Locker, Parcel and ParcelService must all agree that 1, 2 and 3 mean small, medium and large (row 25).

2.2 Name one problem that only SonarQube found, or write none.

java:S106, System.out used instead of a logger, in Main.java:9 and ParcelService.java:30, 67 and 69 (row 6). The AI review did not report it.

2.3 Name one AI finding that was wrong or too vague, and explain why. If every finding was right, explain how you checked them.

Row 13 is partly wrong. The AI says that the same Parcel object can be stored twice, but Main.java:13 creates a new Parcel for every store command, so this cannot happen through the program. I checked each AI finding by opening the file at the line that the AI gave, and I checked rows 1, 2 and 14 against the output of the run in Task 1.

## Task 3. Fix and check

Table 3. What you did with each SonarQube issue in Table 1

| No. in Table 1 | Fixed or kept | Commit, or the reason for keeping it |
| --- | --- | --- |
| 1 | | |

Number of SonarQube issues that remain after your fixes:

The two design problems that you fixed

| Design problem (course term) | What you changed | Commit |
| --- | --- | --- |
| | | |
| | | |

Output of the program after your fixes, for the commands `store P1 6501 2`, `pickup P1 5` and `staff`, and the content of parcels.csv:

```text

```

## Task 4. AI-generated poor and good designs

| Item | bikeshare-poor | bikeshare-good |
| --- | --- | --- |
| Date | | |
| AI tool and exact model | | |
| Prompt (B or C as given, or your changed prompt in full) | | |
| Builds with mvn package (yes or no) | | |
| Number of classes, interfaces, enums and records | | |
| SonarQube issues, total | | |
| SonarQube issues, by rule | | |
| AI review findings in Check 1 | | |
| AI review findings in Check 2 | | |
| Review output file in the reviews folder | | |

4.1 Did SonarQube report any cohesion or coupling problem in bikeshare-poor? Quote the rule and the message if it did.

4.2 Did the AI review tell the two versions apart? Give one finding for bikeshare-poor that does not appear for bikeshare-good.

4.3 Which version would you rather extend with a new member type, such as a visitor who pays 4 baht per minute, and why?

## Task 5. Reflection

5.1 What does SonarQube check, and what does it not check? Answer with examples from this lab.

5.2 When should you not trust the AI review? Give an example from this lab.

5.3 What will you do differently in Project Phase 2 after this lab?

## AI usage declaration

| AI tool and exact model | Purpose | Files or parts that it produced or changed | How you checked its output |
| --- | --- | --- | --- |
| | | | |
