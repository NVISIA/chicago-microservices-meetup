# How to Contribute

Contributions will be accepted via GitHub pull requests. This document outlines some of the 
conventions on development workflow, commit message formatting, and other resources to make 
it easier to get your contribution accepted.

## Contribution Flow

This is a rough outline of what a contributor's workflow looks like:

- Based on corresponding Github issue, create an appropriately typed branch from the main development branch (develop)
- Make commits of logical units.
- Make sure your commit messages are in the proper format (see below).
- Push your changes to branch in origin repository.
- Make sure the tests pass, and add any new tests as appropriate.
- Submit a pull request to the main development branch (or release branch if appropriate).

Thanks for your contributions!

## <a name="rules"></a> Coding Rules 

## <a name="commits"></a> Git Commit Guidelines

We have very precise rules over how our git commit messages can be formatted.  This leads to **more
readable messages** that are easy to follow when looking through the **project history**.  But also,
we _may_ use the git commit messages to **generate release notes**.

The commit message formatting can be added using a typical git workflow or through the use of a CLI
wizard ([Commitizen](https://github.com/commitizen/cz-cli)). To use the wizard, run `yarn run commit`
in your terminal after staging your changes in git.

### Commit Message Format
Each commit message consists of a **header**, a **body** and a **footer**.  The header has a special
format that includes a **type**, and a **subject**:

```
chore: update jenkinsfile for releases

- add release note generate to Jenkinsfile 

Issue: #1
```

```
<type>(optional scope): <subject text>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

### Header
The **header** is mandatory.

The first line is the subject and should be no longer than 70 characters, the
second line is always blank, and other lines should be wrapped at 80 characters.
This allows the message to be easier to read on GitHub as well as in various
git tools. It consists of the following:

#### Type
Must be one of the following:

* **feat**: A new feature
* **fix**: A bug fix
* **docs**: Documentation only changes
* **style**: Changes that do not affect the meaning of the code (white-space, formatting, missing
  semi-colons, etc)
* **refactor**: A code change that neither fixes a bug nor adds a feature (could be performance related)
* **test**: Adding missing or correcting existing tests
* **chore**: Changes to the build process or auxiliary tools and libraries such as documentation generation or Jenkinsfile changes

#### Subject
The subject contains succinct description of the change:

* use the imperative, present tense: "change" not "changed" nor "changes"
* don't capitalize first letter
* no dot (.) at the end

### Body
Just as in the **subject**, use the imperative, present tense: "change" not "changed" nor "changes".
The body should include the motivation for the change and contrast this with previous behavior.

### Footer
The footer should contain any information about **Breaking Changes** and is also the place to
[reference JIRA issues that this commit closes][closing-issues].

**Breaking Changes** should start with the word `BREAKING CHANGE:` with a space or two newlines.
The rest of the commit message is then used for this.

A detailed explanation can be found in this [document][commit-message-format].

### Revert
If the commit reverts a previous commit, it should begin with `revert: `, followed by the header
of the reverted commit.
In the body it should say: `This reverts commit <hash>.`, where the hash is the SHA of the commit
being reverted.
A commit with this format is automatically created by the [`git revert`][git-revert] command.

## <a name="submit-pr"></a> Pull Request Submission Guidelines
Before you submit your pull request consider the following guidelines:

* Make your changes in a new git branch:

    ```shell
    git checkout -b my-fix-branch master
    ```


    ```shell
    git commit -a
    ```
  Note: the optional commit `-a` command line option will automatically "add" and "rm" edited files.

* Before creating the Pull Request, package and run all tests a last time

* Push your branch to GitHub:

    ```shell
    git push origin my-fix-branch
    ```

* In GitHub, submit a pull request and assign appropriately
