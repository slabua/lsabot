#!/usr/bin/perl -w 

use strict;
use IO::File;

my $stopwordfile = 'stopwords';

my $stopw = {};

if( -e $stopwordfile)
{
    my $swfh = IO::File->new($stopwordfile, 'r');
    if( defined $swfh )
    {
        my $line;
        while( defined ( $line = $swfh->getline() ) )
        {
            chomp $line;
            $line =~ s/\'//g; # get rid of apostophes in stop word set
            $stopw->{$line} = 1;
        }
        
        $swfh->close();
    }
    else 
    {
        print STDERR "Can't read from file '$stopwordfile': $!\n";
        exit(1);
    }
}

my $in_run_of_newlines = 0;

my $line;
while ( defined ( $line = <STDIN> ) )
{
    # remove trailing newlines and such
    chomp $line;

    # if line is not all whitespace
    if( $line !~ /^\s*$/ )
    {
        if( $in_run_of_newlines ) 
        {
            print "\n";
            $in_run_of_newlines = 0;
        }

        # lowercase
        $line = lc($line);
        
        # convert all characters that are 'worthy' of a space to a space
        $line =~ s/[\-\/]/ /g;

        # delete anything else that isn't a letter or a space
       ##$line =~ s/[^a-z ]//g;
        # don't delete the dot
        $line =~ s/[^a-z \.\n]//g;
       ## Se si vuole mantenere il punto prima della nuova riga
        #$line =~ s/\./\.\n/g;
       ## Se invece il punto deve essere sostituito dal new line
        #$line =~ s/\./\n/g;

        my @words = split /\s+/, $line; 
        
        # if the line contains any words
        if( scalar @words )
        {
            my $word;
            foreach $word (@words)
            {
                if( not exists $stopw->{$word} )
                {
                    print $word." ";
                }
            }
        }
        
    }
    else
    {
        $in_run_of_newlines = 1;
    }

}
