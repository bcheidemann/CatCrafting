# Print a horizontal divider
alias hr="printf '%*s\n' \"${COLUMNS:-$(tput cols)}\" '' | tr ' ' -"
# Build the project
alias build="hr && echo 'Building CatCrafting jar...' && hr && mvn install && hr && echo Done. || hr; echo Failed."
