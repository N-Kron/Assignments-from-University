
# First, create a file called file1
file1="file1"
touch "$file1"

# Create hard links to that file
currentpath="$(pwd)"
name="link"
linknum=0

# Create links using a loop
while [ $linknum -lt 3 ]; do #condition statement pretty basic loop
    linkname="${name}_$linknum" #this sets the linkfile name
    ln "$currentpath/$file1" "$linkname" #this creates the link if file doesnt exist creates it
    linknum=$((linknum + 1)) #this increments the name so it creates another link
done