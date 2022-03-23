//Author: Mostafa Adel Khodary
//ID: 20211093
//Game 3 -A1- Number Scrabble
#include<iostream>
#include<set>
using namespace std;
set<int> mylist={1, 2, 3, 4, 5, 6, 7, 8, 9};//make a list contain numbers
void display_state(){ // to print a list
    for (int item : mylist)
    cout <<item <<" ";}
int get_input(string player){ // get the number  from players
    bool valid = true;
    while(valid){
        int choosen_number;
        cout<<player<<" please choose number :";
        cin>>choosen_number;
        if(choosen_number == 1 || 2 || 3 || 4 || 5 || 6 || 7 || 8 || 9){
            bool valid = false;}
    return choosen_number;}}
bool find3Numbers(int A[], int arr_size, int sum) // to check if sum of 3 numbers equal 15
{	for (int i = 0; i < arr_size - 2; i++)
	{for (int j = i + 1; j < arr_size - 1; j++)
		{for (int k = j + 1; k < arr_size; k++)
			{if (A[i] + A[j] + A[k] == sum)
				{
				    return true;}
}
        }
            }
    return false;}
int checkDraw(){ // to check if no one win
    if(mylist.empty())
        {
        cout<<" Draw "<<endl;
        cout<<" Game Over";
        return 0;
        }
}
int main(){
    string first_player,second_player;
    cout<<"player 1 PLz enter your name"<<endl; // get player 1 name
    cin>>first_player;
    cout<<"player 2 PLz enter your name"<<endl; // get player 2 name
    cin>>second_player;
    int arr[5],Arr[5]; // make an array for every player
    int counter_1=0,counter_2=0;
    while(true){ // play game
            if (counter_1<=counter_2){
                display_state();
                int first = get_input(first_player); // get player 1 number
                if(mylist.find(first) != mylist.end()){
                    mylist.erase(first);
                    counter_1+=1;
                    arr[counter_1] = first;}
                else{
                    cout<<"this Number not existed in the list "<<endl;
                    continue;}
                if (counter_1>=3){
                    int sum = 15;
                    int arr_size = sizeof(arr) / sizeof(arr[0]);
                    if(find3Numbers(arr,arr_size,sum)){
                        cout<<first_player<<" Congratulations You win "<<endl;
                        cout<<" Game Over ";
                        return 0;}}
                checkDraw();}
            else{
                display_state();
                int second = get_input(second_player);
                if(mylist.find(second) != mylist.end()){
                    mylist.erase(second);
                    counter_2+=1;
                    Arr[counter_2] = second;}
                else{
                    cout<<"this Number not existed in the list "<<endl;
                    continue;}
                if (counter_2>=3){
                    int sum = 15;
                    int arr_size = sizeof(Arr) / sizeof(Arr[0]);
                    if(find3Numbers(Arr,arr_size,sum)){
                        cout<<second_player<<" Congratulations You win "<<endl;
                        cout<<" Game Over ";
                        return 0;}}
                checkDraw();}}}
